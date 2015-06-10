package com.yetu.emscher.mantarepo;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetu.emscher.app.EmscherApp;
import com.yetu.emscher.app.EmscherModule;
import com.yetu.emscher.app.UpdateRepository;
import com.yetu.emscher.app.config.FileRepoConfig;
import com.yetu.emscher.app.config.MantaConfig;
import com.yetu.emscher.app.resources.UpdateResource;
import com.yetu.emscher.filerepo.FileSystemHealthCheck;
import com.yetu.manta.client.MantaClient;

import dagger.Module;
import dagger.Provides;

@Module(injects = { UpdateResource.class, MantaRepo.class, EmscherApp.class }, addsTo = EmscherModule.class, complete = false, library = true)
public class MantaRepoModule {

	private final static Logger logger = LoggerFactory
			.getLogger(MantaRepoModule.class);

	@Provides
	public MantaClient provideMantaClient(MantaConfig config) {
		try {
			return new MantaClient(config.getLogin());
		} catch (IOException e) {
			logger.error("Can't create MantaClient", e);
			return null;
		}
	}

	@Provides
	@Singleton
	public UpdateRepository provideUpdateRepository(MantaClient client,
			MantaConfig config, ObjectMapper mapper) {
		return new MantaRepo(client, config, mapper);
	}

	@Provides
	@Named("repo")
	public HealthCheck provideRepoHealthCheck(MantaClient client,
			MantaConfig config) {
		return new MantaHealthCheck(client, config);
	}

}
