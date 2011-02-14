package com.gueei.demos.fbUpload;

import com.gueei.android.binding.Binder;
import com.gueei.demos.fbUpload.viewModels.UploadImageViewModel;
import com.gueei.demos.fbUpload.SessionEvents.AuthListener;

import android.app.Activity;
import android.os.Bundle;

public class SelectAlbumActivity extends Activity implements AuthListener {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binder.setAndBindContentView(this, R.layout.select_album, this);
        SessionEvents.addAuthListener(this);
        if (!FBUploadApplication.getInstance().requestAuthFacebook(this)){
        	//loadList = true;
        }
    }

	public void onAuthSucceed() {
		// TODO Auto-generated method stub
		
	}

	public void onAuthFail(String error) {
		// TODO Auto-generated method stub
		
	}
}
