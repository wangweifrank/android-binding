package com.gueei.demo.musicplayer;

import gueei.binding.Binder;
import android.app.Application;

public class MusicPlayerApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
	}
}
