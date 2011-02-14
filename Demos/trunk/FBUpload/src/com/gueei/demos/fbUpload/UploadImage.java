package com.gueei.demos.fbUpload;

import com.gueei.android.binding.Binder;
import com.gueei.demos.fbUpload.viewModels.UploadImageViewModel;
import com.gueei.demos.fbUpload.SessionEvents.AuthListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class UploadImage extends Activity {
	UploadImageViewModel mModel;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mModel = new UploadImageViewModel(this);
        Binder.setAndBindContentView(this, R.layout.edit_upload_image, mModel);
    }
	
	public void SelectAlbum(UploadImageViewModel.EditImage[] Images){
		Intent intent = new Intent(this, SelectAlbumActivity.class);
		this.startActivity(intent);
	}
}