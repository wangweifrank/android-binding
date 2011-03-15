package com.gueei.android.binding;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.LayoutInflater.Factory;

import com.gueei.android.binding.bindingProviders.BindingProvider;

public class ViewFactory implements Factory {
	
	public static final String BINDING_NAMESPACE = "http://schemas.android.com/apk/res/com.gueei.android.binding";
	
	private LayoutInflater mInflater;
	
	private ArrayList<View> processedViews = new ArrayList<View>();
	
	public ViewFactory(LayoutInflater inflater){
		this.mInflater = inflater;
	}
		
	protected View CreateViewByInflater(String name, Context context,
			AttributeSet attrs) {
		try {
			String prefix = "android.widget.";
			if ((name=="View") || (name=="ViewGroup"))
				prefix = "android.view.";
			if (name.contains("."))
				prefix = null;
			
			View view = mInflater.createView(name, prefix,
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
		for(int i=0; i<count; i++){
			String attrName = attrs.getAttributeName(i);
			String attrValue = attrs.getAttributeValue(BindingProvider.BindingNamespace, attrName);
			if (attrValue!=null){
				map.put(attrName, attrValue);
			}
		}

		// Only save those with binding attributes
		if (!map.isEmpty()){
			Binder.putBindingMapToView(view, map);
			processedViews.add(view);
		}
		
		return view;
	}
	
	public ArrayList<View> getProcessedViews(){
		return processedViews;
	}
}
