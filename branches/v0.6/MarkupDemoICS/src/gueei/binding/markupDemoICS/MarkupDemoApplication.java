package gueei.binding.markupDemoICS;

import gueei.binding.Binder;
import android.app.Application;

public class MarkupDemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
	}

}
