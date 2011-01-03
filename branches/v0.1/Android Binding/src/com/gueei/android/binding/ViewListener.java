package com.gueei.android.binding;

import java.util.HashMap;

import android.view.View;
import com.gueei.android.binding.listeners.MulticastListener;

class ViewListener{
	private View view;
	private HashMap<Class<?>, MulticastListener<?>> listeners = 
		new HashMap<Class<?>, MulticastListener<?>>();
	
	public ViewListener(View view){
		this.view = view;
	}
	
	public MulticastListener<?> getMulticastListener(Class<?> type){
		if (listeners.containsKey(type)){
			return listeners.get(type);
		}
		MulticastListener<?> listener = MulticastListener.Factory.create(type, view);
		return listener;
	}
}
