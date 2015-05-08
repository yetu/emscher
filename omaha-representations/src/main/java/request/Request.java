package request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.yetu.omaha.App;

public class Request {

	private String protocol;
	private String updaterversion;
	private String installsource;
	private int ismachine;

	private Os os;
	private App app;

	@JacksonXmlProperty(isAttribute = true)
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getUpdaterversion() {
		return updaterversion;
	}

	public void setUpdaterversion(String updaterversion) {
		this.updaterversion = updaterversion;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getInstallsource() {
		return installsource;
	}

	public void setInstallsource(String installsource) {
		this.installsource = installsource;
	}

	@JacksonXmlProperty(isAttribute = true)
	public int getIsmachine() {
		return ismachine;
	}

	public void setIsmachine(int ismachine) {
		this.ismachine = ismachine;
	}

	public Os getOs() {
		return os;
	}

	public void setOs(Os os) {
		this.os = os;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

}
