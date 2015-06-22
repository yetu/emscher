package com.yetu.emscher.mantarepo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RetrofitError;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.yetu.emscher.Metadata;
import com.yetu.emscher.Utils;
import com.yetu.emscher.app.UpdateRepository;
import com.yetu.emscher.app.config.MantaConfig;
import com.yetu.manta.client.MantaClient;
import com.yetu.manta.client.representations.MantaObject;
import com.yetu.omaha.App;
import com.yetu.omaha.Ping;
import com.yetu.omaha.UpdateCheck;
import com.yetu.omaha.response.Action;
import com.yetu.omaha.response.Manifest;
import com.yetu.omaha.response.Package;
import com.yetu.omaha.response.Url;

public class MantaRepo extends CacheLoader<String, App> implements
		UpdateRepository {

	public final static String VERSION_PREFIX = "R39-";
	public final static String VERSION_SUFFIX = "-a1";

	public final static String CHROMEOS_APP_ID = "{87efface-864d-49a5-9bb3-4b050a7c227a}";

	private final static Logger logger = LoggerFactory
			.getLogger(MantaRepo.class);

	private MantaClient mantaClient;
	private MantaConfig mantaConfig;

	private ObjectMapper jsonMapper;

	private LoadingCache<String, App> updateCache;

	public MantaRepo(MantaClient client, MantaConfig config,
			ObjectMapper jsonMapper) {
		this.mantaClient = client;
		this.mantaConfig = config;
		this.jsonMapper = jsonMapper;

		updateCache = CacheBuilder.newBuilder()
				.expireAfterWrite(2, TimeUnit.HOURS).maximumSize(10)
				.build(this);
	}

	private App getUpdateInfo(String currentVersion, String board, String track) {
		String boardSpecificPath = Utils.concatUrl(
				mantaConfig.getUpdateBasePath(), board, track);

		try {
			logger.debug("Listing updates in manta path {}", boardSpecificPath);
			List<MantaObject> updates = getOrderedListOfUpdateFolder(mantaClient
					.listMantaObjects(boardSpecificPath));
			logger.debug("Found {} update folders", updates.size());

			MantaObject nextUpdate = determineUpdateFolder(currentVersion,
					track, updates);
			if (nextUpdate == null) {
				logger.debug("Didn't found a suitable update, returning no update response");
				return createNoUpdate();
			} else {
				logger.debug("Next update is {}", nextUpdate.getName());
				String updatePath = Utils.concatUrl(boardSpecificPath,
						nextUpdate.getName());
				logger.debug("Update path is {}", updatePath);
				try {
					Collection<MantaObject> updateFiles = mantaClient
							.listMantaObjects(updatePath);
					if (updateFiles.size() < 2) {
						logger.error(
								"For update {} were not all necessary files present",
								nextUpdate.getName());
					}
					if (updateFiles.size() > 2) {
						logger.warn(
								"We had more files than necessary in the folder for update {}",
								nextUpdate.getName());
					}
					boolean metadataFound = false;
					boolean payloadFound = false;
					for (MantaObject m : updateFiles) {
						if ("update.gz".equals(m.getName())) {
							payloadFound = true;
						}
						if ("update.meta".equals(m.getName())) {
							metadataFound = true;
						}
					}
					if (!metadataFound) {
						logger.error(
								"We didn't found the update metadata for update {}",
								nextUpdate.getName());
					}
					if (!payloadFound) {
						logger.error(
								"We didn't found the payload file for update {}",
								nextUpdate.getName());
					}
					if (!payloadFound || !metadataFound) {
						return createNoUpdate();
					}
					// TODO get the object via mantaClient and check it
				} catch (RetrofitError error) {
					logger.error(
							"Can't access payload or metadata for update {}",
							nextUpdate.getName(), error);
					return createNoUpdate();
				}
				// FIXME validate these file paths
				logger.debug("Returning update response");
				try {
					return createUpdateResponse(nextUpdate.getName(),
							updatePath, mantaConfig.getUrl(),
							mantaConfig.getUpdateBasePath());
				} catch (IOException e) {
					logger.error(
							"Something failed when creating the update response",
							e);
				}
			}
		} catch (retrofit.RetrofitError error) {
			logger.error("Not possible to access manta", error);
		}

		return createNoUpdate();
	}

	@Override
	public App getUpdateForVersion(App requestApp) {
		String currentVersion = requestApp.getVersion();
		currentVersion = cleanReceivedVersion(currentVersion);
		String board = requestApp.getBoard();
		String track = requestApp.getTrack();

		try {
			App update = updateCache.get(currentVersion + "/" + board + "/"
					+ track);
			if (update != null) {
				return update;
			}
		} catch (ExecutionException e) {
			logger.error("Exception while trying to get update from cache", e);
		}

		return createNoUpdate();
	}

	private App createUpdateResponse(String updatedVerison, String updatePath,
			String baseUrl, String basePath) throws JsonParseException,
			JsonMappingException, IOException {
		App app = new App();
		app.setAppid(CHROMEOS_APP_ID);
		app.setStatus("ok");

		Ping ping = new Ping();
		ping.setStatus("ok");

		UpdateCheck updateCheck = new UpdateCheck();
		updateCheck.setStatus("ok");
		updateCheck.addUrl(new Url(Utils.concatUrl(baseUrl,
				removeBasePathFromUpdatePath(updatePath, basePath))));

		InputStream metadataIs = mantaClient.getObjectInputStream(Utils
				.concatUrl(updatePath, "update.meta"));
		Metadata metadata = jsonMapper.readValue(metadataIs, Metadata.class);

		Package pkg = new Package();
		pkg.setHash(metadata.getSha1());
		pkg.setName("update.gz");
		pkg.setRequired(true);
		pkg.setSize(metadata.getSize());

		updatedVerison = updatedVerison.substring(VERSION_PREFIX.length(),
				updatedVerison.length() - VERSION_SUFFIX.length());

		Action action = new Action();
		action.setChromeOSVersion(updatedVerison);
		action.setEvent("postinstall");
		action.setIsDeltaPayload(metadata.isDelta());
		action.setMetaDataSize(metadata.getMetadataSize());
		action.setNeedsadmin(false);
		action.setSha256(metadata.getSha256());

		Manifest manifest = new Manifest();
		manifest.addAction(action);
		manifest.addPackage(pkg);
		manifest.setVersion(updatedVerison);

		updateCheck.setManifest(manifest);

		app.setPing(ping);
		app.setUpdatecheck(updateCheck);

		app.setVersion(updatedVerison);

		return app;
	}

	private MantaObject determineUpdateFolder(String currentVersion,
			String track, List<MantaObject> objects) {
		if (objects.isEmpty()) {
			logger.warn("No update folders found!");
			return null;
		}
		if ("ForcedUpdate".equals(currentVersion)) {
			return objects.get(objects.size() - 1);
		}

		if ("developer-build".equals(track)) {
			if (isLatestVersionNewerThanReceivedVersion(currentVersion, objects
					.get(objects.size() - 1).getName())) {
				return objects.get(objects.size() - 1);
			} else {
				return null;
			}

		}
		MantaObject nextUpdate = null;
		for (MantaObject m : objects) {
			String updateVersion = m.getName();
			logger.debug("Comparing received version {} with update folder {}",
					currentVersion, updateVersion);
			if (currentVersion.compareTo(updateVersion) < 0) {
				logger.debug("Version {} seems to be newer than {}",
						updateVersion, currentVersion);
				nextUpdate = m;
				break;
			}
		}
		return nextUpdate;
	}

	private List<MantaObject> getOrderedListOfUpdateFolder(
			Collection<MantaObject> objects) {
		List<MantaObject> folders = objects.stream()
				.filter(o -> o.isDirectory() && !o.getName().startsWith("disabled"))
				.sorted(new MantaObjectComparator())
				.collect(Collectors.toList());
		return folders;
	}

	protected String removeBasePathFromUpdatePath(String absoluteUpdatePath,
			String absoluteBasePath) {
		return absoluteUpdatePath.substring(absoluteBasePath.length());
	}

	protected boolean isLatestVersionNewerThanReceivedVersion(
			String receivedVersion, String updateFolder) {
		return updateFolder.compareTo(receivedVersion) > 0;
	}

	protected String cleanReceivedVersion(String receivedVersion) {
		if (!"ForcedUpdate".equals(receivedVersion)) {
			if (!receivedVersion.startsWith(VERSION_PREFIX)) {
				receivedVersion = VERSION_PREFIX + receivedVersion;

			}
			if (!receivedVersion.endsWith(VERSION_SUFFIX)) {
				receivedVersion = receivedVersion + VERSION_SUFFIX;
			}
		}
		return receivedVersion;
	}

	protected App createNoUpdate() {
		App app = new App();
		app.setStatus("ok");
		UpdateCheck check = new UpdateCheck();
		check.setStatus("noupdate");
		app.setUpdatecheck(check);
		Ping ping = new Ping();
		ping.setStatus("ok");
		app.setPing(ping);
		return app;
	}

	@Override
	public App load(String key) throws Exception {
		String[] keyParts = key.split("/");
		if (keyParts.length != 3) {
			throw new IllegalArgumentException(
					"The key does not match expectations. Given key: " + key);
		}
		return getUpdateInfo(keyParts[0], keyParts[1], keyParts[2]);
	}

}
