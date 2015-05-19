package com.yetu.emscher.filerepo;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetu.emscher.app.EmscherModule;
import com.yetu.emscher.app.UpdateRepository;
import com.yetu.emscher.app.config.FileRepoConfig;
import com.yetu.emscher.app.resources.UpdateResource;

import dagger.Module;
import dagger.Provides;

@Module(injects = { UpdateResource.class, FileRepository.class }, addsTo = EmscherModule.class, complete = false, library = true)
public class FileRepoModule {

	@Provides
	@Singleton
	public UpdateRepository provideUpdateRepository(FileRepoConfig config,
			ObjectMapper mapper) {
		return new FileRepository(config, mapper);
	}

}
