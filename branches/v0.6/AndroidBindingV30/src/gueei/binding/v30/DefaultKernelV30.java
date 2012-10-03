package gueei.binding.v30;

import gueei.binding.AttributeBinder;
import gueei.binding.BindingLog;
import gueei.binding.DefaultKernel;
import gueei.binding.v30.bindingProviders.ListViewProviderV30;
import gueei.binding.v30.bindingProviders.ViewPagerViewProviderV30;
import gueei.binding.v30.bindingProviders.ViewProviderV30;
import android.app.Application;

public class DefaultKernelV30 extends DefaultKernel{
	
	protected static final boolean hasCompatibilityLibrarySupport;

    static {
        boolean found = false;
        try {
            Class.forName("android.support.v4.app.Fragment");
            found = true;
        } catch( Exception ignored ) {}
        hasCompatibilityLibrarySupport = found;
    }
    
	@Override
    public void init(Application application) {
		// Put v30 providers first
		getAttributeBinder().registerProvider(new ListViewProviderV30());
		getAttributeBinder().registerProvider(new ViewProviderV30());
		
		if( hasCompatibilityLibrarySupport ) {
			AttributeBinder.getInstance().registerProvider(new ViewPagerViewProviderV30());
		} else {
			BindingLog.warning("BinderV30.init", "android.support.v4.app compatibility library not found");
		}
		
		super.init(application);
    }
}
