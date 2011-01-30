package com.gueei.demos.fbUpload;

import java.util.ArrayList;
import java.util.Set;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.observables.ArraySource;
import com.gueei.android.binding.observables.StringObservable;
import com.gueei.demos.fbUpload.SessionEvents.AuthListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class FBUpload extends Activity implements AuthListener {
	private UploadImageDataViewModel mModel;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mModel = new UploadImageDataViewModel();
        Binder.setAndBindContentView(this, R.layout.main, mModel);
        SessionEvents.addAuthListener(this);
        FBUploadApplication.getInstance().requestAuthFacebook();
    }
    
    public class UploadImageDataViewModel{
    	public ArraySource<EditImageViewModel> EditImageList = new ArraySource<EditImageViewModel>();

    	public UploadImageDataViewModel(){
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            ArrayList<Uri> files = bundle.getParcelableArrayList(Intent.EXTRA_STREAM);
            if (files==null) return;
            EditImageViewModel[] editImageList = new EditImageViewModel[files.size()]; 
            for(int i=0; i<files.size(); i++){
            	editImageList[i] = new EditImageViewModel(files.get(i));
            }
            EditImageList.setArray(editImageList);
    	}
    }
    
    public class EditImageViewModel {
    	public final Observable<Uri> Source = new Observable<Uri>(Uri.class);
    	public final Uri SourceUri;
    	public final Observable<Bitmap> PreviewImage = new Observable<Bitmap>(Bitmap.class);
    	public final StringObservable Caption = new StringObservable();
    	
    	public EditImageViewModel(Uri source){
    		Source.set(source);
    		SourceUri = source;
    		// Start async load of preview Image
    		Thread loadImageThread = new Thread(){
    			Bitmap previewBmp;
				public void run() {
					String id = Source.get().getLastPathSegment();
					previewBmp = MediaStore.Images.Thumbnails.getThumbnail
						(getContentResolver(), Long.parseLong(id), 
								MediaStore.Images.Thumbnails.MICRO_KIND, new BitmapFactory.Options());
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					PreviewImage.set(previewBmp);
				}
    		};
    		loadImageThread.start();
    	}
    }

	
    public void onAuthSucceed() {
    	Toast.makeText(this, "Authed!", Toast.LENGTH_SHORT).show();
	}
    

	public void onAuthFail(String error) {
		
	}
}