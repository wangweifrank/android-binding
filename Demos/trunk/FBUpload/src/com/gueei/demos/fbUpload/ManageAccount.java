package com.gueei.demos.fbUpload;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.observables.StringObservable;
import com.gueei.demos.fbUpload.SessionEvents.AuthListener;

import android.app.Activity;
import android.os.Bundle;

public class ManageAccount extends Activity implements AuthListener {

	public StringObservable Response = new StringObservable("Nothing");
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    SessionEvents.addAuthListener(this);
	    FBUploadApplication.getInstance().requestAuthFacebook();
	    Binder.setAndBindContentView(this, R.layout.manage_account, this);
	}

	public void onAuthSucceed() {
		PostAccounts pa = new PostAccounts(FBUploadApplication.getInstance().getFacebook());
		pa.loadAccounts(Response);
	}

	public void onAuthFail(String error) {
	}

}
