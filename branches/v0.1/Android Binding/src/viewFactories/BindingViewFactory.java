package viewFactories;

import com.gueei.android.binding.Binder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public interface BindingViewFactory {
	public static String defaultNS = "http://code.google.com/p/android-binding";
	
	// Init will be run in first time. This is the time to register for factories
	public void Init();
	
	// Chain of responsibility. If the factory cannot handle, simply returns null
	public View CreateView(String name, Binder binder, Context context, AttributeSet attrs, Object model);
	
	public boolean BindView(View view, Binder binder, AttributeSet attrs, Object model);
}
