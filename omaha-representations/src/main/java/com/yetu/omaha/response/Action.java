package com.yetu.omaha.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Action {

	private String event;
	private String ChromeOSVersion;
	private String sha256;
	private boolean needsadmin;
	private boolean IsDeltaPayload;

	// FIXME: Possible extra attributes

	@JacksonXmlProperty(isAttribute = true)
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@JacksonXmlProperty(isAttribute = true, localName="ChromeOSVersion")
	public String getChromeOSVersion() {
		return ChromeOSVersion;
	}

	public void setChromeOSVersion(String chromeOSVersion) {
		ChromeOSVersion = chromeOSVersion;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	@JacksonXmlProperty(isAttribute = true)
	public boolean isNeedsadmin() {
		return needsadmin;
	}

	public void setNeedsadmin(boolean needsadmin) {
		this.needsadmin = needsadmin;
	}

	@JacksonXmlProperty(isAttribute = true, localName="IsDeltaPayload")
	public boolean isIsDeltaPayload() {
		return IsDeltaPayload;
	}

	public void setIsDeltaPayload(boolean isDeltaPayload) {
		IsDeltaPayload = isDeltaPayload;
	}

}
