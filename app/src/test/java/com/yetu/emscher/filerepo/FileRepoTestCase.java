package com.yetu.emscher.filerepo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetu.emscher.Metadata;
import com.yetu.emscher.Utils;
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

	@Test
	public void testNonDeveloperUpdates() throws Exception {
		FileRepoConfig config = new FileRepoConfig();
		config.setBasePath(new File(UPDATE_BASE_PATH).getAbsolutePath());
		config.setBaseUrl("http://updates.yetu.me/gateway/static");

		FileRepository repo = new FileRepository(config, new ObjectMapper());
		App requestApp = new App();
		requestApp.setBoard("yetu-pfla02");
		requestApp.setTrack("npower-pilot");
		requestApp.setVersion("A");
		App updatedApp = repo.getUpdateForVersion(requestApp);
		Assert.assertEquals("ok", updatedApp.getUpdatecheck().getStatus());
		Assert.assertEquals("B", updatedApp.getVersion());
		Assert.assertTrue(updatedApp.getUpdatecheck().getUrls().iterator()
				.next().getCodebase().endsWith("/"));
		Assert.assertEquals("sha1#B", updatedApp.getUpdatecheck().getManifest()
				.getPackages().iterator().next().getHash());
	}

	@Test
	public void testDisabledUpdates() throws Exception {
		FileRepoConfig config = new FileRepoConfig();
		config.setBasePath(new File(UPDATE_BASE_PATH).getAbsolutePath());
		config.setBaseUrl("http://updates.yetu.me/gateway/static");

		FileRepository repo = new FileRepository(config, new ObjectMapper());
		App requestApp = new App();
		requestApp.setBoard("yetu-pfla02");
		requestApp.setTrack("npower-pilot");
		requestApp.setVersion("B");

		// Test disable via disable file
		File disableFile = new File(Utils.concatUrl(UPDATE_BASE_PATH,
				"yetu-pfla02", "npower-pilot", "R39-C-a1", ".disabled"));
		disableFile.createNewFile();

		App updatedApp = repo.getUpdateForVersion(requestApp);
		Assert.assertEquals("noupdate", updatedApp.getUpdatecheck().getStatus());

		// Test disable via folder name
		Files.move(Paths.get(UPDATE_BASE_PATH, "yetu-pfla02", "npower-pilot",
				"R39-C-a1"), Paths.get(UPDATE_BASE_PATH, "yetu-pfla02",
				"npower-pilot", "disabled_R39-C-a1"),
				StandardCopyOption.ATOMIC_MOVE);

		updatedApp = repo.getUpdateForVersion(requestApp);
		Assert.assertEquals("noupdate", updatedApp.getUpdatecheck().getStatus());
	}

	@Test
	public void testMigrations() throws Exception {
		FileRepoConfig config = new FileRepoConfig();
		config.setBasePath(new File(UPDATE_BASE_PATH).getAbsolutePath());
		config.setBaseUrl("http://updates.yetu.me/gateway/static");

		// Test nomigrationsFile
		File disableFile = new File(Utils.concatUrl(UPDATE_BASE_PATH,
				"yetu-pfla02", "npower-pilot", "R39-B-a1", ".nomigrations"));
		disableFile.createNewFile();

		FileRepository repo = new FileRepository(config, new ObjectMapper());
		App requestApp = new App();
		requestApp.setBoard("yetu-pfla02");
		requestApp.setTrack("npower-pilot");
		requestApp.setVersion("A");

		App updatedApp = repo.getUpdateForVersion(requestApp);
		disableFile.delete();

		Assert.assertEquals("C", updatedApp.getVersion());

	}

	@Test
	public void testLastUpdateIsDisabled() throws Exception {
		FileRepoConfig config = new FileRepoConfig();
		config.setBasePath(new File(UPDATE_BASE_PATH).getAbsolutePath());
		config.setBaseUrl("http://updates.yetu.me/gateway/static");

		// Test disable via disable file
		File disableFile = new File(Utils.concatUrl(UPDATE_BASE_PATH,
				"yetu-pfla02", "npower-pilot", "R39-C-a1", ".disabled"));
		boolean disableFileCreated = disableFile.createNewFile();
		Assert.assertTrue(disableFileCreated);

		// Test nomigrationsFile
		File migrationFile = new File(Utils.concatUrl(UPDATE_BASE_PATH,
				"yetu-pfla02", "npower-pilot", "R39-B-a1", ".nomigrations"));
		migrationFile.createNewFile();
		
		FileRepository repo = new FileRepository(config, new ObjectMapper());
		App requestApp = new App();
		requestApp.setBoard("yetu-pfla02");
		requestApp.setTrack("npower-pilot");
		requestApp.setVersion("A");
		
		App updatedApp = repo.getUpdateForVersion(requestApp);
		
		migrationFile.delete();
		disableFile.delete();

		Assert.assertNotNull(updatedApp);
		Assert.assertEquals("ok", updatedApp.getUpdatecheck().getStatus());
		Assert.assertEquals("B", updatedApp.getVersion());
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
