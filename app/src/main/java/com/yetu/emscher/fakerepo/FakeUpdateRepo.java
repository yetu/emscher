package com.yetu.emscher.fakerepo;

import javax.inject.Inject;

import com.yetu.emscher.app.UpdateRepository;
import com.yetu.omaha.App;

public class FakeUpdateRepo implements UpdateRepository {
	
	public FakeUpdateRepo() {
		
	}

	public App getUpdateForVersion(App requestApp) {
		String currentVersion = requestApp.getVersion();
		String board = requestApp.getBoard();
		String hardwareClass = requestApp.getHardwareClass();
		return null;
	}

}
