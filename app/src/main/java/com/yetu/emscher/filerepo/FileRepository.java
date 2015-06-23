package com.yetu.emscher.filerepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetu.emscher.app.config.FileRepoConfig;
import com.yetu.emscher.repo.AbstractPathBasedRepo;
import com.yetu.emscher.repo.Path;

public class FileRepository extends AbstractPathBasedRepo {

	public final static String VERSION_PREFIX = "R39-";
	public final static String VERSION_SUFFIX = "-a1";

	private final static Logger logger = LoggerFactory
			.getLogger(FileRepository.class);

	private FileRepoConfig config;

	public FileRepository(FileRepoConfig config, ObjectMapper objectMapper) {
		super(objectMapper);
		this.config = config;
		this.jsonMapper = objectMapper;
	}

	@Override
	protected Path getPath(String path) {
		return new FilePath(path);
	}

	@Override
	protected String getUpdateBasePath() {
		return config.getBasePath();
	}

	@Override
	protected String getUpdateBaseUrl() {
		return config.getBaseUrl();
	}

}
