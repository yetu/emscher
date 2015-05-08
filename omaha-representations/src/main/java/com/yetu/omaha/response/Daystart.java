package com.yetu.omaha.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Daystart {

	private long elapsed_seconds;

	@JacksonXmlProperty(isAttribute = true)
	public long getElapsed_seconds() {
		return elapsed_seconds;
	}

	public void setElapsed_seconds(long elapsed_seconds) {
		this.elapsed_seconds = elapsed_seconds;
	}

}
