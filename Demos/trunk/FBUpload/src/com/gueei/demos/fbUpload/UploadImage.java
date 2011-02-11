package com.gueei.demos.fbUpload;

import com.gueei.android.binding.Binder;
import com.gueei.demos.fbUpload.viewModels.UploadImageViewModel;
import com.gueei.demos.fbUpload.SessionEvents.AuthListener;

import android.app.Activity;
import android.os.Bundle;

public class UploadImage extends Activity implements AuthListener {
	UploadImageViewModel mModel;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mModel = new UploadImageViewModel(this);
        Binder.setAndBindContentView(this, R.layout.edit_upload_image, mModel);
        SessionEvents.addAuthListener(this);
        if (!FBUploadApplication.getInstance().requestAuthFacebook()){
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
