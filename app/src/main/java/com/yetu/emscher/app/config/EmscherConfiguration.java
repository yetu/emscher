package com.yetu.emscher.app.config;

import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.bazaarvoice.dropwizard.assets.AssetsBundleConfiguration;
import com.bazaarvoice.dropwizard.assets.AssetsConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmscherConfiguration extends Configuration implements
		AssetsBundleConfiguration {

	private MantaConfig mantaConfig;
	private FileRepoConfig fileConfig;

	@Valid
	@JsonProperty
	private final AssetsConfiguration assets = new AssetsConfiguration();

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

	@JsonProperty("file")
	public FileRepoConfig getFileConfig() {
		return fileConfig;
	}

	@JsonProperty("file")
	public void setFileConfig(FileRepoConfig fileConfig) {
		this.fileConfig = fileConfig;
	}

	@Override
	public AssetsConfiguration getAssetsConfiguration() {
		return assets;
	}

}
