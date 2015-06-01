package com.yetu.emscher.filerepo;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetu.emscher.Metadata;
import com.yetu.emscher.Utils;
import com.yetu.emscher.app.UpdateRepository;
import com.yetu.emscher.app.config.FileRepoConfig;
import com.yetu.omaha.App;
import com.yetu.omaha.Ping;
import com.yetu.omaha.UpdateCheck;
import com.yetu.omaha.response.Action;
import com.yetu.omaha.response.Manifest;
import com.yetu.omaha.response.Package;
import com.yetu.omaha.response.Url;

public class FileRepository implements UpdateRepository {

	public final static String VERSION_PREFIX = "R39-";

	private final static Logger logger = LoggerFactory
			.getLogger(FileRepository.class);

	private FileRepoConfig config;
	private ObjectMapper jsonMapper;

	public FileRepository(FileRepoConfig config, ObjectMapper objectMapper) {
		this.config = config;
		this.jsonMapper = objectMapper;
	}

	@Override
	public App getUpdateForVersion(App requestApp) {
		String currentVersion = requestApp.getVersion();
		if (!"ForcedUpdate".equals(currentVersion)
				&& !currentVersion.startsWith(VERSION_PREFIX)) {
			currentVersion = VERSION_PREFIX + currentVersion;
			logger.debug(
					"Corrected current version, since it was missing the R, it is now {}",
					currentVersion);
		}
		String board = requestApp.getBoard();
		String track = requestApp.getTrack();
		logger.debug(
				"Got update request for board {} with current version {} on track {}",
				board, currentVersion, track);

		URI boardSpecificUri = URI.create(config.getBasePath() + "/" + board
				+ "/" + track);
		Path path = Paths.get(boardSpecificUri);
		File updatePathFile = path.toFile();
		logger.debug(
				"Trying to find a suitable update in filesystem in folder {}",
				updatePathFile.getAbsolutePath());
		if (!updatePathFile.isDirectory()) {
			logger.error("{} is not a directory",
					updatePathFile.getAbsolutePath());
			return createNoUpdate();
		}
		File updateFolder = determineUpdateFolder(currentVersion,
				getOrderedListOfUpdateFolder(Arrays.asList(updatePathFile
						.listFiles())));

		if (updateFolder == null) {
			logger.debug("Didn't found a suitable update, returning no update response");
			return createNoUpdate();
		} else {
			logger.debug("Found suitable update in folder {}",
					updateFolder.getAbsolutePath());
			File updatePayload = new File(updateFolder, "update.gz");
			File metadataFile = new File(updateFolder, "update.meta");
			if (!updatePayload.canRead() || !metadataFile.canRead()) {
				logger.error("Missing update payload or meta data in {}",
						updateFolder.getAbsolutePath());
				return createNoUpdate();
			}
			try {
				logger.debug("Returning update response");
				return createUpdateResponse(updateFolder, requestApp,
						config.getBaseUrl(), config.getBasePath());
			} catch (IOException e) {
				logger.error("Can't read update meta data from folder {}",
						updateFolder.getAbsolutePath(), e);
				return createNoUpdate();
			}
		}
	}

	private String removeBasePathFromUpdatePath(File updatePath, String basePath) {
		String absoluteUpdatePath = updatePath.getAbsolutePath();
		String absoluteBasePath = Paths.get(URI.create(basePath)).toFile()
				.getAbsolutePath();
		return absoluteUpdatePath.substring(absoluteBasePath.length());
	}

	private App createUpdateResponse(File updateFolder, App requestApp,
			String baseUrl, String basePath) throws JsonParseException,
			JsonMappingException, IOException {
		String updatedVersion = updateFolder.getName();
		App app = new App();
		app.setAppid(requestApp.getAppid());
		app.setStatus("ok");

		Ping ping = new Ping();
		ping.setStatus("ok");

		UpdateCheck updateCheck = new UpdateCheck();
		updateCheck.setStatus("ok");
		updateCheck.addUrl(new Url(Utils.concatUrl(baseUrl,
				removeBasePathFromUpdatePath(updateFolder, basePath))));

		File metadataObject = new File(updateFolder, "update.meta");
		Metadata metadata = jsonMapper
				.readValue(metadataObject, Metadata.class);

		Package pkg = new Package();
		pkg.setHash(metadata.getSha1());
		pkg.setName("update.gz");
		pkg.setRequired(true);
		pkg.setSize(metadata.getSize());

		Action action = new Action();
		action.setChromeOSVersion(updatedVersion);
		action.setEvent("postinstall");
		action.setIsDeltaPayload(metadata.isDelta());
		action.setMetaDataSize(metadata.getMetadataSize());
		action.setNeedsadmin(false);
		action.setSha256(metadata.getSha256());

		Manifest manifest = new Manifest();
		manifest.addAction(action);
		manifest.addPackage(pkg);
		manifest.setVersion(updatedVersion);

		updateCheck.setManifest(manifest);

		app.setPing(ping);
		app.setUpdatecheck(updateCheck);
		app.setVersion(updatedVersion);

		return app;
	}

	private File determineUpdateFolder(String currentVersion, List<File> folders) {
		if (folders.isEmpty()) {
			logger.warn("No update folders found!");
			return null;
		}
		if ("ForcedUpdate".equals(currentVersion)) {
			return folders.get(folders.size() - 1);
		}
		File nextUpdate = null;
		for (File f : folders) {
			String updateVersion = f.getName();
			logger.debug("Comparing received version {} with update folder {}",
					currentVersion, updateVersion);
			if (currentVersion.compareTo(updateVersion) < 0) {
				logger.debug("Version {} seems to be newer than {}",
						updateVersion, currentVersion);
				nextUpdate = f;
				break;
			}
		}
		return nextUpdate;
	}

	private List<File> getOrderedListOfUpdateFolder(Collection<File> objects) {
		List<File> folders = objects.stream().filter(o -> o.isDirectory())
				.sorted(new UpdateFolderComparator())
				.collect(Collectors.toList());
		return folders;
	}

	private App createNoUpdate() {
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

}
