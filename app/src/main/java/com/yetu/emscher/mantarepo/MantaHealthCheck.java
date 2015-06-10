package com.yetu.emscher.mantarepo;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck;
import com.yetu.emscher.app.config.MantaConfig;
import com.yetu.manta.client.MantaClient;
import com.yetu.manta.client.representations.MantaObject;

public class MantaHealthCheck extends HealthCheck {

	private final static Logger logger = LoggerFactory
			.getLogger(MantaHealthCheck.class);

	private MantaConfig config;
	private MantaClient client;

	public MantaHealthCheck(MantaClient client, MantaConfig config) {
		this.config = config;
		this.client = client;
	}

	@Override
	protected Result check() throws Exception {

		// Make sure that we have the folder for at least one board
		try {
			Collection<MantaObject> boardFolders = client
					.listMantaObjects(config.getUpdateBasePath());
			if (boardFolders.size() > 0) {
				return Result.healthy();
			} else {
				return Result.unhealthy("No board folders are found");
			}
		} catch (Exception e) {
			logger.error("HealtCheck can't read from manta", e);
			return Result.unhealthy("Can't read from manta", e);
		}
	}
}
