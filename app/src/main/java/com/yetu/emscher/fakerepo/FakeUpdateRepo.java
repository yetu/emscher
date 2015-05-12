package com.yetu.emscher.fakerepo;

import com.yetu.emscher.app.UpdateRepository;
import com.yetu.omaha.App;
import com.yetu.omaha.Ping;
import com.yetu.omaha.UpdateCheck;
import com.yetu.omaha.response.Action;
import com.yetu.omaha.response.Manifest;
import com.yetu.omaha.response.Package;
import com.yetu.omaha.response.Url;

public class FakeUpdateRepo implements UpdateRepository {
	
	public FakeUpdateRepo() {
		
	}

	public App getUpdateForVersion(App requestApp) {
		String currentVersion = requestApp.getVersion();
		String board = requestApp.getBoard();
		String hardwareClass = requestApp.getHardwareClass();
		App updatedApp = new App();
		updatedApp.setAppid(requestApp.getAppid());
		updatedApp.setStatus("ok");
		
		Ping ping = new Ping();
		ping.setStatus("ok");
		
		UpdateCheck updateCheck = new UpdateCheck();
		updateCheck.setStatus("ok");
		updateCheck.addUrl(new Url("https://update.yetu.com/static"));
		
		Package pkg = new Package();
		pkg.setHash("sha based hash");
		pkg.setName("update.gz");
		pkg.setRequired(true);
		pkg.setSize(42);
		
		Action action = new Action();
		action.setChromeOSVersion("9999.0.0");
		action.setEvent("postinstall");
		action.setIsDeltaPayload(false);
		action.setMetaDataSize(23);
		action.setNeedsadmin(false);
		action.setSha256("sha256 based hash");
		
		Manifest manifest = new Manifest();
		manifest.addAction(action);
		manifest.addPackage(pkg);
		manifest.setVersion("9999.0.0");
		
		updateCheck.setManifest(manifest);
		
		updatedApp.setPing(ping);
		updatedApp.setUpdatecheck(updateCheck);
		
		return updatedApp;
	}

}
