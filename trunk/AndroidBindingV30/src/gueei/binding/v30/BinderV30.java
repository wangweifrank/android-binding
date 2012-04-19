package gueei.binding.v30;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.BindingLog;
import gueei.binding.BindingMap;
import gueei.binding.v30.bindingProviders.ViewPagerViewProviderV30;
import gueei.binding.v30.bindingProviders.ViewProviderV30;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BinderV30 extends gueei.binding.Binder {
	
	// idea from roboguice
    protected static final boolean hasCompatibilityLibrarySupport;

    static {
        boolean found = false;
        try {
            Class.forName("android.support.v4.app.Fragment");
            found = true;
        } catch( Exception ignored ) {}
        hasCompatibilityLibrarySupport = found;
    }
	
	public static void init(Application application){
		// Put v30 providers first
		AttributeBinder.getInstance().registerProvider(new ViewProviderV30());
		if( hasCompatibilityLibrarySupport ) {
			AttributeBinder.getInstance().registerProvider(new ViewPagerViewProviderV30());
		} else {
			BindingLog.warning("BinderV30.init", "android.support.v4.app compatibility library not found");
		}
		Binder.init(application);
	}
	
	// This is supposed to be called by fragments
	public static View createAndBindView
		(Context context, int layoutId, ViewGroup container, Object model){
		
		InflateResult result = Binder.inflateView(context, layoutId, container, false);
		return Binder.bindView(context, result, model);
	}
	
	/**
	 * Inflate, and parse the binding information with Android binding
	 * This is different from the original one as it use V30 version of ViewFactory that possibly supports 
	 * Fragment and fragment attributes injection
	 * @param context
	 * @param layoutId The xml layout declaration
	 * @param parent Parent view of the group, just pass null in most cases
	 * @param attachToRoot Pass false
	 * @return Inflate Result. 
	 */
	public static InflateResult inflateView(Context context, int layoutId, ViewGroup parent, boolean attachToRoot){
		LayoutInflater inflater = LayoutInflater.from(context).cloneInContext(context);
		ViewFactoryV30 factory = new ViewFactoryV30(inflater);
		inflater.setFactory2(factory);
		InflateResult result = new InflateResult();
		result.rootView = inflater.inflate(layoutId, parent, attachToRoot);
		result.processedViews = factory.getProcessedViews();
		return result;
	}
	
	/**
	 * Merge the BindingMap in the view with the supplied new map
	 * If both keys are existed, it will be replaced by the new map
	 * @param view
	 * @param map
	 */
	public static void mergeBindingMap(View view, BindingMap map){
		BindingMap original = getBindingMapForView(view);
		if (original==null){
			putBindingMapToView(view, map);
			return;
		}
		
		for(String key: map.getAllKeys()){
			original.put(key, map.get(key));
		}
		putBindingMapToView(view, original);
	}
}
