package com.gueei.tests;

import java.util.ArrayList;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.observables.ArraySource;
import com.gueei.android.binding.observables.StringObservable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class ImageReceiver extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Binder.setAndBindContentView(this, R.layout.main, new ViewModel());
	}

	public class ViewModel{
		public ArraySource<EditImageModel> ImageList = new ArraySource<EditImageModel>();
		public ViewModel(){
			ArrayList<Uri> uris = getIntent().getExtras().getParcelableArrayList(Intent.EXTRA_STREAM);
		    EditImageModel[] images = new EditImageModel[uris.size()];
		    for(int i=0; i<uris.size(); i++){
		    	images[i] = new EditImageModel();
		    	images[i].Caption.set("item: " + i);
		    	images[i].Path.set(uris.get(i));
		    }
		    ImageList.setArray(images);
		}
	}
	
	public class EditImageModel{
		public StringObservable Caption = new StringObservable();
		public Observable<Uri> Path = new Observable<Uri>(Uri.class);
		public DependentObservable<Bitmap> ImageThumb = 
			new DependentObservable<Bitmap>(Bitmap.class, Path){
				@Override
				public Bitmap calculateValue(Object... args) throws Exception {
					if (Path.get() == null )return null;
					long id = Long.parseLong(Path.get().getLastPathSegment());
					return MediaStore.Images.Thumbnails.getThumbnail
						(getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, new BitmapFactory.Options());
				}
		};
	}
}
