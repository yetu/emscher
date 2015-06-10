package com.yetu.emscher.repo;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;

import com.yetu.emscher.app.UpdateRepository;
import com.yetu.omaha.App;
import com.yetu.omaha.Ping;
import com.yetu.omaha.UpdateCheck;

public abstract class AbstractPathBasedRepo implements UpdateRepository {

	public final static String VERSION_PREFIX = "R39-";
	public final static String VERSION_SUFFIX = "-a1";

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

}
