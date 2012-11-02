package gueei.binding.treeviewdemo;

import gueei.binding.Binder;
import gueei.binding.labs.BinderTreeview;
import android.app.Application;

public class TreeviewDemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
		Binder.init(this);
		BinderTreeview.init(this);
	}

}
