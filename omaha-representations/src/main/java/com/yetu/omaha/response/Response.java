package com.yetu.omaha.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="response")
public class Response {

	private String protocol;
	private Daystart daystart;
	private App app;

	@JacksonXmlProperty(isAttribute = true)
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Daystart getDaystart() {
		return daystart;
	}

	public void setDaystart(Daystart daystart) {
		this.daystart = daystart;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

}
