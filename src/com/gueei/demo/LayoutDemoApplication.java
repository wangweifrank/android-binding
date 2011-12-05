package com.gueei.demo;

import gueei.binding.Binder;
import android.app.Application;

public class LayoutDemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
	}

}
