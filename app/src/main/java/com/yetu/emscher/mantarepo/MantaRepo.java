package com.yetu.emscher.mantarepo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetu.emscher.Metadata;
import com.yetu.emscher.Utils;
import com.yetu.emscher.app.config.MantaConfig;
import com.yetu.emscher.repo.AbstractPathBasedRepo;
import com.yetu.manta.client.MantaClient;
import com.yetu.manta.client.representations.MantaObject;
import com.yetu.omaha.App;
import com.yetu.omaha.Ping;
import com.yetu.omaha.UpdateCheck;
import com.yetu.omaha.response.Action;
import com.yetu.omaha.response.Manifest;
import com.yetu.omaha.response.Package;
import com.yetu.omaha.response.Url;

public class MantaRepo extends AbstractPathBasedRepo {

	private final static Logger logger = LoggerFactory
			.getLogger(MantaRepo.class);

	private MantaClient mantaClient;
	private MantaConfig mantaConfig;

	private ObjectMapper jsonMapper;

	public MantaRepo(MantaClient client, MantaConfig config,
			ObjectMapper jsonMapper) {
		this.mantaClient = client;
		this.mantaConfig = config;
		this.jsonMapper = jsonMapper;
	}

	@Override
	public App getUpdateForVersion(App requestApp) {
		String currentVersion = requestApp.getVersion();
		currentVersion = cleanReceivedVersion(currentVersion);
		String board = requestApp.getBoard();
		String track = requestApp.getTrack();

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
				String updatePayloadPath = Utils.concatUrl(updatePath,
						"update.gz");
				String metadataPath = Utils
						.concatUrl(updatePath, "update.meta");
				// FIXME validate these file paths
				logger.debug("Returning update response");
				try {
					return createUpdateResponse(nextUpdate.getName(),
							updatePath, requestApp, mantaConfig.getUrl(),
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

	private App createUpdateResponse(String updatedVerison, String updatePath,
			App requestApp, String baseUrl, String basePath)
			throws JsonParseException, JsonMappingException, IOException {
		App app = new App();
		app.setAppid(requestApp.getAppid());
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
				.filter(o -> o.isDirectory())
				.sorted(new MantaObjectComparator())
				.collect(Collectors.toList());
		return folders;
	}

}
