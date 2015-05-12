package com.yetu.emscher.app;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.Feature;
import com.yetu.emscher.app.config.EmscherConfiguration;
import com.yetu.emscher.app.resources.TestResource;
import com.yetu.emscher.app.resources.UpdateEngineRequestFilter;
import com.yetu.emscher.app.resources.UpdateResource;
import com.yetu.emscher.fakerepo.FakeModule;
import com.yetu.emscher.mantarepo.MantaModule;
import com.yunspace.dropwizard.xml.XmlBundle;

import dagger.ObjectGraph;

public class EmscherApp extends Application<EmscherConfiguration> {

	private ObjectGraph objectGraph;

	private Environment environment;

	public static void main(String[] args) throws Exception {
		new EmscherApp().run(args);
	}

	@Override
	public void initialize(Bootstrap<EmscherConfiguration> bootstrap) {
		XmlBundle xmlBundle = new XmlBundle();
		xmlBundle.getXmlMapper().configure(Feature.WRITE_XML_DECLARATION, true);
		xmlBundle.getXmlMapper().enable(SerializationFeature.INDENT_OUTPUT);
		bootstrap.addBundle(xmlBundle);
		super.initialize(bootstrap);
	}

	@Override
	public void run(EmscherConfiguration configuration, Environment environment)
			throws Exception {
		this.environment = environment;
		objectGraph = getObjectGraph(configuration);

		environment.jersey().register(UpdateEngineRequestFilter.class);

		registerResource(UpdateResource.class);
		registerResource(TestResource.class);
	}

	private void registerResource(Class<?> resourceClass) {
		environment.jersey().register(objectGraph.get(resourceClass));
	}

	private ObjectGraph getObjectGraph(EmscherConfiguration config) {
		Object updateModule = null;
		if ("fake".equals(config.getRepo())) {
			updateModule = new FakeModule();
		} else if ("manta".equals(config.getRepo())) {
			updateModule = new MantaModule();
		}
		return ObjectGraph.create(new EmscherModule(config)).plus(updateModule);
	}

}
