package com.yetu.omaha;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class App {

	private String appid;
	private String status;
	private Ping ping;
	private UpdateCheck updatecheck;
	
	private String version;
	private String track;
	private String lang;
	private String board;
	private String hardwareClass;
	private boolean deltaOkay;
	private String fwVersion;
	private String ecVersion;

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

	@JacksonXmlProperty(isAttribute = true)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	@JacksonXmlProperty(isAttribute = true, localName="hardware_class")
	public String getHardwareClass() {
		return hardwareClass;
	}

	public void setHardwareClass(String hardwareClass) {
		this.hardwareClass = hardwareClass;
	}

	@JacksonXmlProperty(isAttribute = true, localName="delta_okay")
	public boolean isDeltaOkay() {
		return deltaOkay;
	}

	public void setDeltaOkay(boolean deltaOkay) {
		this.deltaOkay = deltaOkay;
	}

	@JacksonXmlProperty(isAttribute = true, localName="fw_version")
	public String getFwVersion() {
		return fwVersion;
	}

	public void setFwVersion(String fwVersion) {
		this.fwVersion = fwVersion;
	}

	@JacksonXmlProperty(isAttribute = true, localName="ec_version")
	public String getEcVersion() {
		return ecVersion;
	}

	public void setEcVersion(String ecVersion) {
		this.ecVersion = ecVersion;
	}

}
