package com.yetu.omaha.response;

import java.util.Collection;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class UpdateCheck {

	private Collection<Url> urls;
	private Manifest manifest;
	private String status;

	public Collection<Url> getUrls() {
		return urls;
	}

	public void setUrls(Collection<Url> urls) {
		this.urls = urls;
	}

	public Manifest getManifest() {
		return manifest;
	}

	public void setManifest(Manifest manifest) {
		this.manifest = manifest;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
