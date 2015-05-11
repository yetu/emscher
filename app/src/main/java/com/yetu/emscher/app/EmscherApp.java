package com.yetu.emscher.app;

import com.yetu.emscher.app.resources.UpdateResource;

import dagger.ObjectGraph;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EmscherApp extends Application<EmscherConfiguration> {

	private ObjectGraph objectGraph;

	private Environment environment;
	
	public static void main(String[] args) throws Exception {
		new EmscherApp().run(args);
	}

	@Override
	public void run(EmscherConfiguration configuration, Environment environment)
			throws Exception {
		this.environment = environment;
		objectGraph = getObjectGraph(configuration);
		registerResource(UpdateResource.class);
	}

	private void registerResource(Class<?> resourceClass) {
		environment.jersey().register(objectGraph.get(resourceClass));
	}

	private ObjectGraph getObjectGraph(EmscherConfiguration config) {
		return ObjectGraph.create(new EmscherModule(config));
	}

}
