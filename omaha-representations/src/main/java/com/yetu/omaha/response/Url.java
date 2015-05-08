package com.yetu.omaha.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Url {

	private String codebase;

	@JacksonXmlProperty(isAttribute = true)
	public String getCodebase() {
		return codebase;
	}

	public void setCodebase(String codebase) {
		this.codebase = codebase;
	}

}
