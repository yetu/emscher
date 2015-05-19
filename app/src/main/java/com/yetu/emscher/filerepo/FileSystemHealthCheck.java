package com.yetu.emscher.filerepo;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.codahale.metrics.health.HealthCheck;
import com.yetu.emscher.app.config.FileRepoConfig;

public class FileSystemHealthCheck extends HealthCheck {

	private FileRepoConfig config;

	public FileSystemHealthCheck(FileRepoConfig config) {
		this.config = config;
	}

	@Override
	protected Result check() throws Exception {
		try {
			Path updateRootPath = Paths.get(URI.create(config.getBasePath()));
			File updateRoot = updateRootPath.toFile();
			Result result = null;
			if (!updateRoot.exists()) {
				result = Result.unhealthy("Update root path does not exist");
			} else if (!updateRoot.isDirectory()) {
				result = Result
						.unhealthy("The update root path is not a directory");
			} else if (!updateRoot.canRead()) {
				result = Result.unhealthy("Can not read update root path");
			} else {
				result = Result.healthy("Yay");
			}
			return result;
		} catch (Exception e) {
			return Result.unhealthy(e);
		}
	}

}
