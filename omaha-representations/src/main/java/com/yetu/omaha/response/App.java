package com.yetu.omaha.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class App {

	private String appid;
	private String status;
	private Ping ping;
	private UpdateCheck updatecheck;

	@JacksonXmlProperty(isAttribute = true)
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Ping getPing() {
		return ping;
	}

	public void setPing(Ping ping) {
		this.ping = ping;
	}

	public UpdateCheck getUpdatecheck() {
		return updatecheck;
	}

	public void setUpdatecheck(UpdateCheck updatecheck) {
		this.updatecheck = updatecheck;
	}

}
