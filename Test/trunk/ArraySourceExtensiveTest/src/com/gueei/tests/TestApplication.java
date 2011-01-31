package com.gueei.tests;

import com.gueei.android.binding.Binder;

import android.app.Application;

public class TestApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
	}

}
