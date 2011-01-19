package com.gueei.demo.contactmanager;

import com.gueei.android.binding.Binder;

import android.app.Application;

public class ContactManagerApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
	}
}
