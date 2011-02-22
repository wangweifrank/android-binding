package com.example.binding.demo;

import com.gueei.android.binding.Binder;

import android.app.Application;

public class DemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
	}

}
