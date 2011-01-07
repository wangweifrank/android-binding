package com.gueei.android.binding;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.gueei.android.binding.converters.ConverterBase;

import android.view.View;

public class BindedView {
	private String name;
	private View view;
	// private HashMap<String, Class<?>> AttributesDefinition = new HashMap<String, Class<?>>(); 
	private HashMap<String, ViewAttribute<?>> Attributes= new HashMap<String, ViewAttribute<?>>();
	private HashMap<String, ConverterBase<?,?>> Converters= new HashMap<String, ConverterBase<?,?>>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	// Build attribute table
	public <T> ViewAttribute<T> addAttribute(
			String attributeName, Method getter, Method setter, Class<T> type){
		try {
			ViewAttribute<T> attr = new ViewAttribute<T>(view, attributeName, getter, setter);
			Attributes.put(attributeName, attr); 
			return attr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void PutConverter(String attributeName, ConverterBase<?,?> converter){
		Converters.put(attributeName, converter);
	}
	
	public ViewAttribute<?> getAttribute(String name){
		return Attributes.get(name);
	}
}
