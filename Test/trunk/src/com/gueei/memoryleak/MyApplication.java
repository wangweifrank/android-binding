package com.gueei.memoryleak;

import gueei.binding.v30.BinderV30;
import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		BinderV30.init(this);
	}

}
