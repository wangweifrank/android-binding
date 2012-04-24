package gueei.binding.markupDemoICS;

import gueei.binding.v30.BinderV30;
import android.app.Application;

public class MarkupDemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		BinderV30.init(this);
	}

}
