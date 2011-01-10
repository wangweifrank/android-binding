package com.gueei.android.binding;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.LayoutInflater.Factory;

public class ViewFactory implements Factory {
	
	public static final String BINDING_NAMESPACE = "http://schemas.android.com/apk/res/com.gueei.android.binding";
	
	private Object model;
	
	public ViewFactory(Object model){
		this.model = model;
	}
	
	private Binder mBinder;
	public ViewFactory(Binder binder){
		mBinder = binder;
	}
	
	protected View CreateViewByInflater(String name, Context context,
			AttributeSet attrs) {
		try {
			String prefix = "android.widget.";
			if ((name=="View") || (name=="ViewGroup"))
				prefix = "android.view.";
			if (name.contains("."))
				prefix = null;
			
			View view = LayoutInflater.from(context).createView(name, prefix,
					attrs);
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		View view = this.CreateViewByInflater(name, context, attrs);
		if (view==null) return null;
		
		BindingMap map = new BindingMap();
		int count = attrs.getAttributeCount();
		
		Binder.putBindingMapToView(view, map);
		
		AttributeBinder.getInstance().mapBindings(view, context, attrs, map);
		AttributeBinder.getInstance().bindView(view, model);
		
		return view;
	}	
}
