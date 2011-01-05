package com.gueei.android.binding;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import android.view.View;
import com.gueei.android.binding.listeners.MulticastListener;

class ViewListener{
	private View view;
	private HashMap<Class<MulticastListener<?>>, MulticastListener<?>> listeners = 
		new HashMap<Class<MulticastListener<?>>, MulticastListener<?>>();
	
	public ViewListener(View view){
		this.view = view;
	}
	
	public <M extends MulticastListener<?>>M getMulticastListener(Class<M> type){
		if (listeners.containsKey(type)){
			return (M) listeners.get(type);
		}
		M listener;
		try {
			listener = (M) type.getConstructor().newInstance();
			listener.registerToView(view);
			return listener;
		} catch (Exception e){
			return null;
		}
	}
}
