package request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Event {

	private int eventtype;
	private int eventresult;
	private String previousversion;

	@JacksonXmlProperty(isAttribute = true)
	public int getEventtype() {
		return eventtype;
	}

	public void setEventtype(int eventtype) {
		this.eventtype = eventtype;
	}

	@JacksonXmlProperty(isAttribute = true)
	public int getEventresult() {
		return eventresult;
	}

	public void setEventresult(int eventresult) {
		this.eventresult = eventresult;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getPreviousversion() {
		return previousversion;
	}

	public void setPreviousversion(String previousversion) {
		this.previousversion = previousversion;
	}

}
