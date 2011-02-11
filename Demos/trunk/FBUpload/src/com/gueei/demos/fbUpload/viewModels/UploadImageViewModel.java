package com.gueei.demos.fbUpload.viewModels;

import java.util.ArrayList;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.collections.IRowModel;
import com.gueei.android.binding.observables.ArraySource;
import com.gueei.android.binding.observables.BooleanObservable;
import com.gueei.android.binding.observables.StringObservable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

public class UploadImageViewModel{
	private Activity mActivity;
	
	public UploadImageViewModel(Activity activity){
		mActivity =activity;
		fillImageList();
	}
	
	private void fillImageList() {
		ArrayList<Uri> files = mActivity.getIntent().getExtras().getParcelableArrayList(Intent.EXTRA_STREAM);
		int size = files.size();
		EditImage[] images = new EditImage[size];
		for(int i=0; i<size; i++){
			images[i] = new EditImage(files.get(i));
		}
		Images.setArray(images);
	}

	public ArraySource<EditImage> Images = new ArraySource<EditImage>();
	
	public class EditImage implements IRowModel {
    	public final StringObservable Caption = new StringObservable();
    	public final Observable<Uri> Source = new Observable<Uri>(Uri.class);
    	public final Observable<Bitmap> PreviewImage = new Observable<Bitmap>(Bitmap.class);
    	public EditImage(Uri source){
    		Source.set(source);
			Caption.set(source.getLastPathSegment());
    	}
    	Thread loadImageThread;
		public void onAttachedToUI() {
			Log.d("Binder", "onAttached" + Source.get());
			if (loadImageThread!=null) return;
			loadImageThread = new Thread(){
    			Bitmap previewBmp;
				public void run() {
					String id = Source.get().getLastPathSegment();
					try {
						previewBmp = MediaStore.Images.Thumbnails.getThumbnail
							(mActivity.getContentResolver(), Long.parseLong(id), 
									MediaStore.Images.Thumbnails.MINI_KIND, new BitmapFactory.Options());
						Thread.sleep(1500);
						PreviewImage.set(previewBmp);
					} catch (InterruptedException e) {
						MediaStore.Images.Thumbnails.cancelThumbnailRequest(mActivity.getContentResolver(), Long.parseLong(id));
						e.printStackTrace();
					}
				}
    		};
    		loadImageThread.start();
		}
    }
}
