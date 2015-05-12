package com.yetu.omaha.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Event {

	private Integer eventtype;
	private Integer eventresult;
	private Integer errorcode;
	private String previousversion;
	private String status;

	@JacksonXmlProperty(isAttribute = true)
	public Integer getEventtype() {
		return eventtype;
	}

	public void setEventtype(Integer eventtype) {
		this.eventtype = eventtype;
	}

	@JacksonXmlProperty(isAttribute = true)
	public Integer getEventresult() {
		return eventresult;
	}

	public void setEventresult(Integer eventresult) {
		this.eventresult = eventresult;
	}

	@JacksonXmlProperty(isAttribute = true)
	public Integer getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getPreviousversion() {
		return previousversion;
	}

	public void setPreviousversion(String previousversion) {
		this.previousversion = previousversion;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
