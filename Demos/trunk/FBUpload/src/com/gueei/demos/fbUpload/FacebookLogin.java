package com.gueei.demos.fbUpload;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FacebookLogin extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    FBUploadApplication.getInstance().getFacebook().authorize(this, new String[]{
	    		"publish_stream", "offline_access", "manage_pages"
	    }, Facebook.FORCE_DIALOG_AUTH, new DialogListener(){
			public void onComplete(Bundle values) {
				SessionStore.save(FBUploadApplication.getInstance().getFacebook(), FBUploadApplication.getInstance());
				SessionEvents.onLoginSuccess();
				finish();
			}
			public void onFacebookError(FacebookError e) {
				e.printStackTrace();
				SessionEvents.onLoginError(e.getErrorType());
			}
			public void onError(DialogError e) {
				e.printStackTrace();
				SessionEvents.onLoginError(e.getMessage());
			}
			public void onCancel() {
			}});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		FBUploadApplication.getInstance().getFacebook().authorizeCallback(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
}
