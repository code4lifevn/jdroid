package com.jdroid.android.sample;

import com.jdroid.android.context.AppContext;

public class TestAppContext extends AppContext {

	@Override
	protected String getServerName() {
		return null;
	}

	@Override
	public String getInstallationSource() {
		return "GooglePlay";
	}

	@Override
	public String getBuildType() {
		return "test";
	}

	@Override
	public String getLocalIp() {
		return null;
	}

	@Override
	public Boolean isStrictModeEnabled() {
		return false;
	}
}
