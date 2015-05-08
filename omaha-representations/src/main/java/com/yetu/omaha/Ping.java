package com.yetu.omaha;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Ping {

	private String status;

	@JacksonXmlProperty(isAttribute = true)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
