package com.jdroid.android.firebase.admob;

import com.jdroid.android.context.AbstractAppContext;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigHelper;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.StringUtils;

import java.util.Set;

public class AdMobAppContext extends AbstractAppContext {

	public static final String TEST_AD_UNIT_ID_ENABLED = "TEST_AD_UNIT_ID_ENABLED";

	public Boolean areAdsEnabledByDefault() {
		return FirebaseRemoteConfigHelper.getBoolean(AdMobRemoteConfigParameter.ADS_ENABLED);
	}

	/**
	 * @return Whether the application has ads enabled or not
	 */
	public Boolean areAdsEnabled() {
		Boolean prefEnabled = SharedPreferencesHelper.get().loadPreferenceAsBoolean(AdMobRemoteConfigParameter.ADS_ENABLED.getKey(), areAdsEnabledByDefault());
		Boolean enoughDaysSinceFirstAppLoad = DateUtils.millisecondsToDays(UsageStats.getFirstAppLoadTimestamp()) >= getMinDaysSinceFirstAppLoad();
		Boolean enoughAppLoads = UsageStats.getAppLoads() >= getMinAppLoadsToDisplayAds() ;
		return prefEnabled && enoughDaysSinceFirstAppLoad && enoughAppLoads;
	}

	protected Long getMinAppLoadsToDisplayAds() {
		return 5L;
	}

	protected Long getMinDaysSinceFirstAppLoad() {
		return 7L;
	}

	public Boolean isTestAdUnitIdEnabled() {
		return SharedPreferencesHelper.get().loadPreferenceAsBoolean(TEST_AD_UNIT_ID_ENABLED, true);
	}

	/**
	 * @return The MD5-hashed ID of the devices that should display mocked ads
	 */
	public Set<String> getTestDevicesIds() {
		String testDevicesIds = getBuildConfigValue("ADS_TEST_DEVICES_IDS", null);
		return testDevicesIds != null ? Sets.newHashSet(StringUtils.splitToCollectionWithCommaSeparator(testDevicesIds)) : Sets.<String>newHashSet();
	}

	/**
	 * @return The AdMob Publisher ID
	 */
	public String getDefaultAdUnitId() {
		return FirebaseRemoteConfigHelper.getString(AdMobRemoteConfigParameter.DEFAULT_AD_UNIT_ID);
	}

	public String getAdMobAppId() {
		return FirebaseRemoteConfigHelper.getString(AdMobRemoteConfigParameter.ADMOB_APP_ID);
	}
}
