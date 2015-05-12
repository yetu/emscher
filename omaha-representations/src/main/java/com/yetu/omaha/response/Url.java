package com.yetu.omaha.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="url")
public class Url {

	private String codebase;
	
	public Url() {
		
	}
	
	public Url(String codebase) {
		this();
		this.codebase = codebase;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getCodebase() {
		return codebase;
	}

	public void setCodebase(String codebase) {
		this.codebase = codebase;
	}

}
