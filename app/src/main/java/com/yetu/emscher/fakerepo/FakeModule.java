package com.yetu.emscher.fakerepo;

import javax.inject.Singleton;

import com.yetu.emscher.app.EmscherModule;
import com.yetu.emscher.app.UpdateRepository;
import com.yetu.emscher.app.resources.UpdateResource;

import dagger.Module;
import dagger.Provides;

@Module(injects = { FakeUpdateRepo.class, UpdateResource.class }, addsTo = EmscherModule.class, complete = false, library = true)
public class FakeModule {

	@Provides
	@Singleton
	public UpdateRepository provideUpdateRepository() {
		return new FakeUpdateRepo();
	}

}
