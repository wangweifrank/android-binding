package com.gueei.demo.abgallery;

import gueei.binding.v30.BinderV30;
import android.app.Application;

public class ABGalleryApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		BinderV30.init(this);
	}

}
