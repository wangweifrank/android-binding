package gueei.binding.v30;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.v30.bindingProviders.ViewProviderV30;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class BinderV30 extends gueei.binding.Binder {
	public static void init(Application application){
		// Put v30 providers first
		AttributeBinder.getInstance().registerProvider(new ViewProviderV30());
		Binder.init(application);
	}
	
	// This is supposed to be called by fragments
	public static View createAndBindView
		(Context context, int layoutId, ViewGroup container, Object model){
		
		InflateResult result = Binder.inflateView(context, layoutId, container, false);
		return Binder.bindView(context, result, model);
	}
}
