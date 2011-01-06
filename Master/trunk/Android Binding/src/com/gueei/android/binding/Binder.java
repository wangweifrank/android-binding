package com.gueei.android.binding;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.WeakHashMap;

import com.gueei.android.binding.exception.BindingException;
import com.gueei.android.binding.listeners.MulticastListener;

import android.R;
import android.util.Log;
import android.view.View;

public class Binder {
	public static final int STOP_PROPAGATION_TAG = R.id.addToDictionary;
	
	private ArrayList<AttributeBind<?>> bindtable = new ArrayList<AttributeBind<?>>();
	private WeakHashMap<String, BindedView> nameViewMap = new WeakHashMap<String, BindedView>();

	public WeakHashMap<String, BindedView> getNameViewMap(){
		return nameViewMap;
	}
	
	public BindedView findViewByName(String name){
		return nameViewMap.get(name);
	}
	
	public enum ViewListeners {
		OnClickListener, OnCreateContextMenuListener, OnFocusChangeListener, OnKeyListener, OnLongClickListener, OnTouchListener
	}

	public void register(Class<?> viewClass, String attributeName,
			Class<?> attributeType, Method getter, Method setter,
			ViewListeners changeListeners) {

	}

	public <T, V extends View> void bind(V view, String attributeName,
			Method getter, Method setter, Observable<T> observableProperty) {
		AttributeBind<T> bind = new AttributeBind<T>(view, attributeName,
				getter, setter, observableProperty);
	}

	public <T, V extends View, M extends MulticastListener<?>> 
		void bind(V view, String attributeName,
			Method getter, Method setter, Observable<T> observableProperty, 
			Class<M>[] associatedListenerTypes) {
		
		AttributeBind<T> bind = new AttributeBind<T>(view, attributeName,
				getter, setter, observableProperty);
		for(Class<M> listenerType : associatedListenerTypes){
			getViewListener(view).getMulticastListener(listenerType).register(bind);
		}
	}
	
	public <T, V extends View, M extends MulticastListener<?>> 
		void bind(V view, String attributeName,
			Method getter, Method setter, Observable<T> observableProperty, 
			Class<M> associatedListenerType) {
		AttributeBind<T> bind = new AttributeBind<T>(view, attributeName,
				getter, setter, observableProperty);
		getViewListener(view).getMulticastListener(associatedListenerType).register(bind);
	}

	private HashMap<View, ViewListener> viewListeners = 
		new HashMap<View, ViewListener>();
	
	public ViewListener getViewListener(View v){
		if (viewListeners.containsKey(v)) return viewListeners.get(v);
		ViewListener vl = new ViewListener(v);
		viewListeners.put(v, vl);
		return vl;
	}
	
	public <M extends MulticastListener<?>> void bindCommand(View v, Class<M> listenerType, Command command) throws BindingException{
		MulticastListener<?> ml = this.getViewListener(v).getMulticastListener(listenerType);
		if (ml!=null)
			ml.register(command);
		else
			throw new BindingException("Multicast listener is not registered"); 
	}
}