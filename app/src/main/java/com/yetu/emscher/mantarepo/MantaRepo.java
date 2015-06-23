package com.yetu.emscher.mantarepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetu.emscher.app.config.MantaConfig;
import com.yetu.emscher.repo.AbstractPathBasedRepo;
import com.yetu.emscher.repo.Path;
import com.yetu.manta.client.MantaClient;
import com.yetu.manta.client.representations.MantaObject;

public class MantaRepo extends AbstractPathBasedRepo {

	private final static Logger logger = LoggerFactory
			.getLogger(MantaRepo.class);

	private MantaClient mantaClient;
	private MantaConfig mantaConfig;

	public MantaRepo(MantaClient client, MantaConfig config,
			ObjectMapper jsonMapper) {
		super(jsonMapper);
		this.mantaClient = client;
		this.mantaConfig = config;
	}

	@Override
	protected Path getPath(String path) {
		MantaObject object = mantaClient.getMantaObject(path);
		return new MantaPath(mantaClient, object, path);
	}

	@Override
	protected String getUpdateBasePath() {
		return mantaConfig.getUpdateBasePath();
	}

	@Override
	protected String getUpdateBaseUrl() {
		return mantaConfig.getUrl();
	}

}
