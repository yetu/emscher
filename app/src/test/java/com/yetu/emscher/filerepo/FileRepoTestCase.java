package com.yetu.emscher.filerepo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetu.emscher.Metadata;
import com.yetu.emscher.app.config.FileRepoConfig;
import com.yetu.omaha.App;

public class FileRepoTestCase {

	private final static String UPDATE_BASE_PATH = "updates";
	private final static String BOARD = "yetu-pfla02";
	private final static String CHANNEL = "developer-build";

	@BeforeClass
	public static void prepareTestPaths() throws Exception {

		createValidUpdateFolder(CHANNEL, "A");
		createValidUpdateFolder(CHANNEL, "B");
		createValidUpdateFolder(CHANNEL, "C");

		createValidUpdateFolder("npower-pilot", "A");
		createValidUpdateFolder("npower-pilot", "B");
		createValidUpdateFolder("npower-pilot", "C");
	}

	private static void createValidUpdateFolder(String channel,
			String versionname) throws IOException {
		File updateBasePath = new File(UPDATE_BASE_PATH);
		File boardRoot = new File(updateBasePath, BOARD);
		File channelRoot = new File(boardRoot, channel);
		File versionRoot = new File(channelRoot, "R39-" + versionname + "-a1");
		versionRoot.mkdirs();
		//Files.copy(Paths.get("src/test/resources/update.meta.a"),
		//		Paths.get(versionRoot.getAbsolutePath(), "update.meta"));

		Metadata metadata = new Metadata();
		metadata.setDelta(false);
		metadata.setMetadataHash("hash#" + versionname);
		metadata.setMetadataSize(23);
		metadata.setSha1("sha1#" + versionname);
		metadata.setSha256("sha256#" + versionname);
		metadata.setSize(42);

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(versionRoot, "update.meta"), metadata);

		Files.copy(Paths.get("src/test/resources/metadata_hash"),
				Paths.get(versionRoot.getAbsolutePath(), "metadata_hash"));
		File payload = new File(versionRoot, "update.gz");
		payload.createNewFile();
	}

	@Test
	public void testNoAvailableUpdate() throws Exception {
		FileRepoConfig config = new FileRepoConfig();
		config.setBasePath(new File(UPDATE_BASE_PATH).getAbsolutePath());
		config.setBaseUrl("http://updates.yetu.me/gateway/static");

		FileRepository repo = new FileRepository(config, new ObjectMapper());
		App requestApp = new App();
		requestApp.setBoard("yetu-pfla02");
		requestApp.setTrack(CHANNEL);
		requestApp.setVersion("C");
		App updatedApp = repo.getUpdateForVersion(requestApp);
		Assert.assertEquals("noupdate", updatedApp.getUpdatecheck().getStatus());
	}

	@Test
	public void testUpdateFromVersionA() throws Exception {
		FileRepoConfig config = new FileRepoConfig();
		config.setBasePath(new File(UPDATE_BASE_PATH).getAbsolutePath());
		config.setBaseUrl("http://updates.yetu.me/gateway/static");

		FileRepository repo = new FileRepository(config, new ObjectMapper());
		App requestApp = new App();
		requestApp.setBoard("yetu-pfla02");
		requestApp.setTrack(CHANNEL);
		requestApp.setVersion("A");
		App updatedApp = repo.getUpdateForVersion(requestApp);
		Assert.assertEquals("ok", updatedApp.getUpdatecheck().getStatus());
		Assert.assertEquals("C", updatedApp.getVersion());
		Assert.assertEquals("sha1#C", updatedApp.getUpdatecheck().getManifest()
				.getPackages().iterator().next().getHash());
	}

	@AfterClass
	public static void cleanup() throws Exception {
		File updateBasePath = new File(UPDATE_BASE_PATH);
		delete(updateBasePath);
	}

	private static void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			throw new FileNotFoundException("Failed to delete file: " + f);
	}
}
