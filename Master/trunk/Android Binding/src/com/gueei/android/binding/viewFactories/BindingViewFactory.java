package com.gueei.android.binding.viewFactories;

import com.gueei.android.binding.BindedView;
import com.gueei.android.binding.Binder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class BindingViewFactory {
	public static String defaultNS = "http://code.google.com/p/android-binding";
	
	// Init will be run in first time. This is the time to register for factories
	public void Init(){}
	
	// Chain of responsibility. If the factory cannot handle, simply returns null
	public View CreateView(String name, Context context, AttributeSet attrs){
		return null;
	}
	
	public abstract boolean BindView(BindedView view, Binder binder, ViewFactory.AttributeMap attrs, Object model);
	
	public abstract void CreateViewAttributes(BindedView view, Binder binder);
	
}
