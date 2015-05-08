package request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Os {

	private String version;
	private String platform;
	private String sp;

	@JacksonXmlProperty(isAttribute = true)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

}
