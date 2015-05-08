package com.yetu.omaha.response;

import java.util.ArrayList;
import java.util.List;

public class ResponseBuilder {

	private List<Action> actions = new ArrayList<Action>();
	private App app = new App();
	private Daystart daystart = new Daystart();
	private Manifest manifest = new Manifest();
	private List<Package> packages = new ArrayList<Package>();
	private Ping ping = new Ping();
	private UpdateCheck updateCheck = new UpdateCheck();
	private List<Url> urls = new ArrayList<Url>();
	private Response response = new Response();

	public static ResponseBuilder create() {
		return new ResponseBuilder();
	}

	public ResponseBuilder addAction(String chromeOsVersion, String event,
			boolean isDeltaPayload, boolean needsAdmin, String sha256) {
		Action action = new Action();
		action.setChromeOSVersion(chromeOsVersion);
		action.setEvent(event);
		action.setIsDeltaPayload(isDeltaPayload);
		action.setNeedsadmin(needsAdmin);
		action.setSha256(sha256);
		actions.add(action);
		return this;
	}
	
	public ResponseBuilder withAppId(String appId) {
		app.setAppid(appId);
		return this;
	}
	
	public ResponseBuilder withPingStatus(String status) {
		this.ping.setStatus(status);
		this.app.setPing(ping);
		return this;
	}
	
	public ResponseBuilder withAppStatus(String appStatus) {
		app.setStatus(appStatus);
		return this;
	}
	
	public ResponseBuilder withManifestVersion(String manifestVersion){
		manifest.setVersion(manifestVersion);
		return this;
	}
	
	public ResponseBuilder addPackage(String hash, String name, boolean required, long size){
		Package pkg = new Package();
		pkg.setHash(hash);
		pkg.setName(name);
		pkg.setRequired(required);
		pkg.setSize(size);
		packages.add(pkg);
		return this;
	}
	
	public ResponseBuilder withUpdateCheckStatus(String status) {
		updateCheck.setStatus(status);
		return this;
	}
	
	public ResponseBuilder addUrl(String codebase) {
		Url url = new Url();
		url.setCodebase(codebase);
		return this;
	}
	
	public ResponseBuilder withProtocolVersion(String version) {
		response.setProtocol(version);
		return this;
	}
	
	public ResponseBuilder withElapsedSeconds(long seconds) {
		daystart.setElapsed_seconds(seconds);
		// TODO: Determine automatically
		return this;
	}
	
	public Response build() {
		
		manifest.setActions(actions);
		manifest.setPackages(packages);
		
		updateCheck.setManifest(manifest);
		updateCheck.setUrls(urls);
		
		app.setPing(ping);
		app.setUpdatecheck(updateCheck);
		
		response.setApp(app);
		response.setDaystart(daystart);
		
		return response;
	}

}
