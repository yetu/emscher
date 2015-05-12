package com.yetu.omaha.response;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Manifest {

	private String version;
	private Collection<Package> packages;
	private Collection<Action> action;

	@JacksonXmlProperty(isAttribute = true)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@JacksonXmlElementWrapper(localName="packages", useWrapping=true)
	@JacksonXmlProperty(localName="package")
	public Collection<Package> getPackages() {
		return packages;
	}

	public void setPackages(Collection<Package> packages) {
		this.packages = packages;
	}

	public void addPackage(Package pkg) {
		if (this.packages == null) {
			this.packages = new ArrayList<Package>();
		}
		this.packages.add(pkg);
	}

	@JacksonXmlElementWrapper(localName="actions", useWrapping=true)
	@JacksonXmlProperty(localName="action")
	public Collection<Action> getActions() {
		return action;
	}

	public void setActions(Collection<Action> actions) {
		this.action = actions;
	}
	
	public void addAction(Action action){
		if(this.action == null){
			this.action = new ArrayList<Action>();
		}
		this.action.add(action);
	}

}
