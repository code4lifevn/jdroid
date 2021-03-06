package com.jdroid.android.application;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.analytics.AnalyticsTracker;

import java.util.List;

public interface AppModule {

	/*
	 * Since Android O, have a guaranteed life cycle limited to 10 seconds for this method execution.
	 */
	@WorkerThread
	public void onInstanceIdTokenRefresh();

	@MainThread
	public void onGooglePlayServicesUpdated();
	
	@MainThread
	public void onInitializeGcmTasks();

	@MainThread
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity);

	@MainThread
	public FragmentDelegate createFragmentDelegate(Fragment fragment);

	// Module Analytics

	public AnalyticsSender<? extends AnalyticsTracker> createModuleAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers);

	public List<? extends AnalyticsTracker> createModuleAnalyticsTrackers();

	public AnalyticsSender<? extends AnalyticsTracker> getModuleAnalyticsSender();

}
