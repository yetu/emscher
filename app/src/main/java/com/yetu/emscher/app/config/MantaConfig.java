package com.yetu.emscher.app.config;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MantaConfig {

	@NotEmpty
	private String url;

	private String login;

	private String privateKey;

	private String privateKeyPath;

	private String keyFingerprint;
	@NotEmpty
	private String updateBasePath;

	@JsonProperty
	public String getUrl() {
		return url;
	}

	@JsonProperty
	public void setUrl(String url) {
		this.url = url;
	}

	@JsonProperty
	public String getLogin() {
		return login;
	}

	@JsonProperty
	public void setLogin(String login) {
		this.login = login;
	}

	@JsonProperty
	public String getPrivateKey() {
		return privateKey;
	}

	@JsonProperty
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	@JsonProperty
	public String getKeyFingerprint() {
		return keyFingerprint;
	}

	@JsonProperty
	public void setKeyFingerprint(String keyFingerprint) {
		this.keyFingerprint = keyFingerprint;
	}

	@JsonProperty
	public String getUpdateBasePath() {
		return updateBasePath;
	}

	@JsonProperty
	public void setUpdateBasePath(String updateBasePath) {
		this.updateBasePath = updateBasePath;
	}

	@JsonProperty
	public String getPrivateKeyPath() {
		return privateKeyPath;
	}

	@JsonProperty
	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}

}
