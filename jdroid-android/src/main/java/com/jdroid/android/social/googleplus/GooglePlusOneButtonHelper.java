package com.jdroid.android.social.googleplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusOneButton.OnPlusOneClickListener;

public class GooglePlusOneButtonHelper implements ConnectionCallbacks, OnConnectionFailedListener {
	
	private static final int PLUS_ONE_REQUEST_CODE = 100;
	private static final int PLUS_ONE_UNDO_REQUEST_CODE = 101;
	private static final String PLAY_STORE_BASE_URL = "https://play.google.com/store/apps/details?id=";
	
	private String url;
	private PlusClient plusClient;
	protected PlusOneButton plusOneButton;
	private Fragment context;
	
	public GooglePlusOneButtonHelper(Fragment context, int plusOneButton) {
		this(context, (PlusOneButton)context.getView().findViewById(plusOneButton));
	}
	
	public GooglePlusOneButtonHelper(Fragment context, PlusOneButton plusOneButton) {
		this.context = context;
		plusClient = new PlusClient.Builder(context.getActivity(), this, this).clearScopes().build();
		this.plusOneButton = plusOneButton;
		url = getUrl();
	}
	
	public void onStart() {
		plusClient.connect();
	}
	
	public void onResume() {
		
		plusOneButton.initialize(url, new OnPlusOneClickListener() {
			
			@Override
			public void onPlusOneClick(Intent intent) {
				if (intent != null) {
					if (intent.getAction().toLowerCase().contains("undo")) {
						context.startActivityForResult(intent, PLUS_ONE_UNDO_REQUEST_CODE);
					} else {
						context.startActivityForResult(intent, PLUS_ONE_REQUEST_CODE);
					}
				}
			}
		});
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == PLUS_ONE_UNDO_REQUEST_CODE) && (resultCode != 0)) {
			onUndoPlusOne();
		} else if ((requestCode == PLUS_ONE_REQUEST_CODE) && (resultCode != 0)) {
			onPlusOne();
		}
	}
	
	public void onStop() {
		plusClient.disconnect();
	}
	
	protected void onPlusOne() {
		// Do Nothing
	}
	
	protected void onUndoPlusOne() {
		// Do Nothing
	}
	
	protected String getUrl() {
		return PLAY_STORE_BASE_URL + context.getActivity().getPackageName();
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// Do Nothing
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle arg0) {
		// Do Nothing
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		// Do Nothing
	}
}