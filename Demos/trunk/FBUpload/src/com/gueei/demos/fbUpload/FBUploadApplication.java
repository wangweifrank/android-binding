package com.gueei.demos.fbUpload;

import com.facebook.android.Facebook;
import com.gueei.android.binding.Binder;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

public class FBUploadApplication extends Application {

	private Facebook mFacebook;
	private static final String AppId = "196687477012645";
	private static FBUploadApplication mInstance;
	private static FacebookAccountRepository mAccountRepo;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
		mInstance = this;
		mAccountRepo = new FacebookAccountRepository(this);
		mAccountRepo.open();
	}
	
	public static FBUploadApplication getInstance(){
		return mInstance;
	}

	public boolean requestAuthFacebook(Activity callingActivity){
		if (getFacebook().isSessionValid()){
			SessionEvents.onLoginSuccess();
			return true;
		}
		// Start Facebook Login
		Intent intent = new Intent(this, FacebookLogin.class);
		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		callingActivity.startActivity(intent);
		return false;
	}
	
	public Facebook getFacebook(){
		if (mFacebook!=null)
			return mFacebook;
		
		mFacebook = new Facebook(AppId);
		SessionStore.restore(mFacebook, this);
		return mFacebook;
	}
	
	public FacebookAccountRepository getAccountRepository(){
		return mAccountRepo;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		mAccountRepo.close();
	}
	
}
