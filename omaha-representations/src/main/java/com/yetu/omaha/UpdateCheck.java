package com.yetu.omaha;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.yetu.omaha.response.Manifest;
import com.yetu.omaha.response.Url;

public class UpdateCheck {

	private Collection<Url> urls;
	private Manifest manifest;
	private String status;

	private String targetversionprefix;

	@JacksonXmlElementWrapper(localName="urls", useWrapping=true)
	@JacksonXmlProperty(localName="url")
	public Collection<Url> getUrls() {
		return urls;
	}

	public void setUrls(Collection<Url> urls) {
		this.urls = urls;
	}
	
	public void addUrl(Url url){
		if(this.urls == null){
			this.urls = new ArrayList<Url>();
		}
		this.urls.add(url);
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

	@JacksonXmlProperty(isAttribute = true)
	public String getTargetversionprefix() {
		return targetversionprefix;
	}

	public void setTargetversionprefix(String targetversionprefix) {
		this.targetversionprefix = targetversionprefix;
	}

}
