package com.yetu.emscher.app;

import javax.inject.Inject;
import javax.inject.Named;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.bazaarvoice.dropwizard.assets.ConfiguredAssetsBundle;
import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.Feature;
import com.yetu.emscher.app.config.EmscherConfiguration;
import com.yetu.emscher.app.resources.TestResource;
import com.yetu.emscher.app.resources.UpdateEngineRequestFilter;
import com.yetu.emscher.app.resources.UpdateResource;
import com.yetu.emscher.fakerepo.FakeModule;
import com.yetu.emscher.filerepo.FileRepoModule;
import com.yunspace.dropwizard.xml.XmlBundle;

import dagger.ObjectGraph;

public class EmscherApp extends Application<EmscherConfiguration> {

	private ObjectGraph objectGraph;

	private Environment environment;

	@Inject
	@Named("repo")
	HealthCheck repoHealthCheck;

	public static void main(String[] args) throws Exception {
		new EmscherApp().run(args);
	}

	@Override
	public void initialize(Bootstrap<EmscherConfiguration> bootstrap) {
		XmlBundle xmlBundle = new XmlBundle();
		xmlBundle.getXmlMapper().configure(Feature.WRITE_XML_DECLARATION, true);
		xmlBundle.getXmlMapper().enable(SerializationFeature.INDENT_OUTPUT);
		bootstrap.addBundle(xmlBundle);
		bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/static/"));

		super.initialize(bootstrap);
	}

	@Override
	public void run(EmscherConfiguration configuration, Environment environment)
			throws Exception {
		this.environment = environment;
		objectGraph = getObjectGraph(configuration);
		objectGraph.inject(this);

		environment.healthChecks().register("repo", repoHealthCheck);

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
		} else if ("file".equals(config.getRepo())) {
			updateModule = new FileRepoModule();
		}
		return ObjectGraph.create(new EmscherModule(config)).plus(updateModule);
	}

}
