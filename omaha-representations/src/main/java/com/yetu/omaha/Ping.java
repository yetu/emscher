package com.yetu.omaha;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Ping {

	private String status;
	private int active;
	private int a;
	private int r;

	@JacksonXmlProperty(isAttribute = true)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JacksonXmlProperty(isAttribute = true)
	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	@JacksonXmlProperty(isAttribute = true)
	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	@JacksonXmlProperty(isAttribute = true)
	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

}
