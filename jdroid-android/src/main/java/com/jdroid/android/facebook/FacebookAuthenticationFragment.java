package com.jdroid.android.facebook;

import org.slf4j.Logger;
import android.content.Intent;
import android.os.Bundle;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.utils.LoggerUtils;

public abstract class FacebookAuthenticationFragment<T extends FacebookAuthenticationUseCase> extends AbstractFragment
		implements SessionStateListener, FacebookAuthenticationListener {
	
	private final Logger logger = LoggerUtils.getLogger(getClass());
	
	private FacebookConnector facebookConnector;
	private T facebookAuthenticationUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		facebookAuthenticationUseCase = createFacebookAuthenticationUseCase();
		facebookConnector = new FacebookConnector(getActivity(), this, this);
		facebookConnector.onCreate(savedInstanceState);
	}
	
	@SuppressWarnings("unchecked")
	protected T createFacebookAuthenticationUseCase() {
		return (T)new FacebookAuthenticationUseCase();
	}
	
	public T getFacebookAuthenticationUseCase() {
		return facebookAuthenticationUseCase;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(facebookAuthenticationUseCase, this);
		facebookConnector.onResume();
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(facebookAuthenticationUseCase, this);
		facebookConnector.onPause();
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		facebookConnector.onDestroy();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebookConnector.onActivityResult(requestCode, resultCode, data);
	}
	
	protected Logger getLogger() {
		return logger;
	}
	
	public void startLoginProcess() {
		facebookConnector.login(this);
	}
	
	public void startLoginForPublishProcess() {
		facebookConnector.loginForPublish(this);
	}
	
	public void startLogoutProcess() {
		facebookConnector.logout();
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookAuthenticationListener#onFacebookLoginCompleted(com.jdroid.android.facebook.FacebookConnector)
	 */
	@Override
	public void onFacebookLoginCompleted(FacebookConnector facebookConnector) {
		facebookAuthenticationUseCase.setLoginMode(true);
		facebookAuthenticationUseCase.setFacebookConnector(facebookConnector);
		executeFacebookLoginUseCase(facebookConnector);
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookAuthenticationListener#onFacebookLogoutCompleted()
	 */
	@Override
	public void onFacebookLogoutCompleted() {
		facebookAuthenticationUseCase.setLoginMode(false);
		executeUseCase(facebookAuthenticationUseCase);
	}
	
	// The onStartUseCase is overridden here to use the executeOnUIThread implementation
	// for fragment that uses internally SafeExecuteWrapperRunnable to check if the fragment
	// is in the resumed state. This is to fix an issue detected returning from Facebook Login,
	// when "Don't keep activities" option is turned on, in this case the loading never dismissed.
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		this.executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				showLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				if (facebookAuthenticationUseCase.isLoginMode()) {
					onFacebookLoginUseCaseFinished(facebookAuthenticationUseCase.getFacebookUser());
				} else {
					onFacebookLogoutUseCaseFinised();
				}
				dismissLoading();
			}
		});
	}
	
	/**
	 * Called when {@link FacebookAuthenticationUseCase} finishes. This method is called from the UI thread.
	 * 
	 * @param facebookUser contains the basic info of the user.
	 */
	protected void onFacebookLoginUseCaseFinished(FacebookUser facebookUser) {
		// Do nothing
	}
	
	protected void onFacebookLogoutUseCaseFinised() {
		// Do nothing
	}
	
	private void executeFacebookLoginUseCase(FacebookConnector facebookConnector) {
		facebookAuthenticationUseCase.setLoginMode(true);
		facebookAuthenticationUseCase.setFacebookConnector(facebookConnector);
		executeUseCase(facebookAuthenticationUseCase);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#goBackOnError(java.lang.RuntimeException)
	 */
	@Override
	public Boolean goBackOnError(RuntimeException runtimeException) {
		return false;
	}
	
	/**
	 * @see com.jdroid.android.facebook.SessionStateListener#onSessionOpened()
	 */
	@Override
	public void onSessionOpened() {
		logger.debug("onSessionOpened()");
	}
	
	/**
	 * @see com.jdroid.android.facebook.SessionStateListener#onSessionClosed()
	 */
	@Override
	public void onSessionClosed() {
		logger.debug("onSessionClosed()");
	}
	
	/**
	 * @see com.jdroid.android.facebook.SessionStateListener#onSessionOpenedWithUpdatedToken()
	 */
	@Override
	public void onSessionOpenedWithUpdatedToken() {
		logger.debug("onSessionOpenedWithUpdatedToken()");
	}
	
	/**
	 * @see com.jdroid.android.facebook.SessionStateListener#onSessionClosedLoginFailed()
	 */
	@Override
	public void onSessionClosedLoginFailed() {
		logger.debug("onSessionClosedLoginFailed()");
	}
	
}
