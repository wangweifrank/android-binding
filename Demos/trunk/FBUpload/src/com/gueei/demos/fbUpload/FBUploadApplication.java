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
	
	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
		mInstance = this;
	}
	
	public static FBUploadApplication getInstance(){
		return mInstance;
	}

	public void requestAuthFacebook(){
		if (getFacebook().isSessionValid()){
			SessionEvents.onLoginSuccess();
			return;
		}
		// Start Facebook Login
		Intent intent = new Intent(this, FacebookLogin.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
	}
	
	public Facebook getFacebook(){
		if (mFacebook!=null)
			return mFacebook;
		
		mFacebook = new Facebook(AppId);
		SessionStore.restore(mFacebook, this);
		return mFacebook;
	}
	
}
