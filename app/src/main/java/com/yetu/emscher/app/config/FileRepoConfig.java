package com.yetu.emscher.app.config;

import org.hibernate.validator.constraints.NotEmpty;

public class FileRepoConfig {

	@NotEmpty
	private String basePath;
	private String baseUrl;

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

}
