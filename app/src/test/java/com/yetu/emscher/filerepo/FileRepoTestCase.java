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
import com.yetu.emscher.app.config.FileRepoConfig;
import com.yetu.omaha.App;

public class FileRepoTestCase {

	private final static String UPDATE_BASE_PATH = "updates";
	private final static String BOARD = "yetu-pfla02";
	private final static String CHANNEL = "developer-build";

	@BeforeClass
	public static void prepareTestPaths() throws Exception {
		File updateBasePath = new File(UPDATE_BASE_PATH);
		File boardRoot = new File(updateBasePath, BOARD);
		File channelRoot = new File(boardRoot, CHANNEL);

		File versionA = new File(channelRoot, "R39-A-a1");
		File versionB = new File(channelRoot, "R39-B-a1");
		versionA.mkdirs();
		versionB.mkdirs();

		Files.copy(Paths.get("src/test/resources/update.meta.a"),
				Paths.get(versionA.getAbsolutePath(), "update.meta"));
		Files.copy(Paths.get("src/test/resources/update.meta.b"),
				Paths.get(versionB.getAbsolutePath(), "update.meta"));

		File payloadA = new File(versionA, "update.gz");
		File payloadB = new File(versionB, "update.gz");
		payloadA.createNewFile();
		payloadB.createNewFile();
	}

	@Test
	public void testNoAvailableUpdate() throws Exception {
		FileRepoConfig config = new FileRepoConfig();
		config.setBasePath(new File(UPDATE_BASE_PATH).toURI().toString());
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
	public void testUpdateFromVersionAtoB() throws Exception {
		FileRepoConfig config = new FileRepoConfig();
		config.setBasePath(new File(UPDATE_BASE_PATH).toURI().toString());
		config.setBaseUrl("http://updates.yetu.me/gateway/static");

		FileRepository repo = new FileRepository(config, new ObjectMapper());
		App requestApp = new App();
		requestApp.setBoard("yetu-pfla02");
		requestApp.setTrack(CHANNEL);
		requestApp.setVersion("A");
		App updatedApp = repo.getUpdateForVersion(requestApp);
		Assert.assertEquals("ok", updatedApp.getUpdatecheck().getStatus());
		Assert.assertEquals("B", updatedApp.getVersion());
		Assert.assertEquals("JXq3p0Cxn2TZlfHG8A+f0eNHaD8=B", updatedApp
				.getUpdatecheck().getManifest().getPackages().iterator().next()
				.getHash());
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
