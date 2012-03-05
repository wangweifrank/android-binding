package com.gueei.demo.inputvalidation;

import gueei.binding.Binder;
import android.app.Application;

public class InputValidationDemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
	}

}
