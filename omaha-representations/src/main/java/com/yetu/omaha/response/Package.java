package com.yetu.omaha.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Package {
	
	private String hash;
	private String name;
	private long size;
	private boolean required;
	
	@JacksonXmlProperty(isAttribute = true)
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	@JacksonXmlProperty(isAttribute=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@JacksonXmlProperty(isAttribute=true)
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	@JacksonXmlProperty(isAttribute=true)
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}

}
