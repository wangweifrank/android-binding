package gueei.binding.test;

import gueei.binding.Binder;
import gueei.binding.v30.BinderV30;
import android.app.Application;

public class AndroidBindingUnitTestApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		BinderV30.init(this);
	}

}
