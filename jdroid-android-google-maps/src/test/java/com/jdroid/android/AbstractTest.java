package com.jdroid.android;

import android.os.Build;

import com.jdroid.android.debug.http.HttpDebugConfiguration;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "src/test/AndroidManifest.xml", sdk = Build.VERSION_CODES.M)
public abstract class AbstractTest {

	@Before
	public final void setUp() throws Exception {
		HttpDebugConfiguration.setHttpMockEnabled(isHttpMockEnabled());
		onSetup();
	}

	protected void onSetup() {
		// Do nothing
	}

	protected abstract Boolean isHttpMockEnabled();
}
