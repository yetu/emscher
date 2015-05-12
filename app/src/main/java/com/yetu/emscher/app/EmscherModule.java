package com.yetu.emscher.app;

import com.yetu.emscher.app.config.EmscherConfiguration;
import com.yetu.emscher.app.config.MantaConfig;
import com.yetu.emscher.app.resources.TestResource;

import dagger.Module;
import dagger.Provides;

@Module(injects = { TestResource.class }, complete = true, library = true)
public class EmscherModule {

	private EmscherConfiguration config;

	public EmscherModule(EmscherConfiguration config) {
		this.config = config;
	}

	@Provides
	public MantaConfig provideMantaConfig() {
		return config.getMantaConfig();
	}

	/*
	 * @Provides
	 * 
	 * @Singleton public UpdateRepository provideUpdateRepository() { return
	 * null; }
	 */

}
