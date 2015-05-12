package com.yetu.omaha;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Ping {

	private String status;
	private Integer active;
	private Integer a;
	private Integer r;

	@JacksonXmlProperty(isAttribute = true)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JacksonXmlProperty(isAttribute = true)
	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	@JacksonXmlProperty(isAttribute = true)
	public Integer getA() {
		return a;
	}

	public void setA(Integer a) {
		this.a = a;
	}

	@JacksonXmlProperty(isAttribute = true)
	public Integer getR() {
		return r;
	}

	public void setR(Integer r) {
		this.r = r;
	}

}
