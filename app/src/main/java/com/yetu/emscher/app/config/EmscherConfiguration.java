package com.yetu.emscher.app.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class EmscherConfiguration extends Configuration {

	private MantaConfig mantaConfig;

	@NotNull
	private String repo;

	@JsonProperty("manta")
	public MantaConfig getMantaConfig() {
		return mantaConfig;
	}

	@JsonProperty("manta")
	public void setMantaConfig(MantaConfig mantaConfig) {
		this.mantaConfig = mantaConfig;
	}

	@JsonProperty
	public String getRepo() {
		return repo;
	}

	@JsonProperty
	public void setRepo(String repo) {
		this.repo = repo;
	}

}
