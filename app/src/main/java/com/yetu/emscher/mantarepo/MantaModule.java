package com.yetu.emscher.mantarepo;

import javax.inject.Singleton;

import com.yetu.emscher.app.EmscherModule;
import com.yetu.emscher.app.UpdateRepository;
import com.yetu.emscher.app.config.MantaConfig;
import com.yetu.emscher.app.resources.UpdateResource;

import dagger.Module;
import dagger.Provides;

@Module(injects = { MantaRepo.class, UpdateResource.class }, addsTo = EmscherModule.class, complete = false, library = true)
public class MantaModule {

	public MantaModule() {

	}

	@Provides
	@Singleton
	public UpdateRepository provideUpdateRepository(MantaConfig config) {
		return new MantaRepo(config);
	}

}
