package com.yetu.emscher.app;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetu.emscher.app.config.EmscherConfiguration;
import com.yetu.emscher.app.config.FileRepoConfig;
import com.yetu.emscher.app.config.MantaConfig;

import dagger.Module;
import dagger.Provides;

@Module(injects = { }, complete = true, library = true)
public class EmscherModule {

	private EmscherConfiguration config;

	public EmscherModule(EmscherConfiguration config) {
		this.config = config;
	}

	@Provides
	public MantaConfig provideMantaConfig() {
		return config.getMantaConfig();
	}

	@Provides
	public FileRepoConfig provideFileRepoConfig() {
		return config.getFileConfig();
	}

	@Provides
	@Singleton
	public ObjectMapper provideJsonObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		return mapper;
	}

}
