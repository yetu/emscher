package com.yetu.emscher.app;

import javax.inject.Singleton;

import com.yetu.emscher.app.resources.UpdateResource;
import com.yetu.emscher.fakerepo.FakeUpdateRepo;

import dagger.Module;
import dagger.Provides;

@Module(injects = { UpdateResource.class, FakeUpdateRepo.class }, library = true )
public class EmscherModule {

	private EmscherConfiguration config;

	public EmscherModule(EmscherConfiguration config) {
		this.config = config;
	}

	@Provides
	@Singleton
	public UpdateRepository provideUpdateRepo() {
		return new FakeUpdateRepo();
	}

}
