package com.gueei.android.binding;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.gueei.android.binding.exception.BindingException;
import com.gueei.android.binding.listeners.MulticastListener;

import android.util.Log;
import android.view.View;

public class Binder {
	private ArrayList<AttributeBind<?>> bindtable = new ArrayList<AttributeBind<?>>();

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

	// May have more arguments like converters, one/two-way binding etc.
	public <T, V extends View> void bind(V view, String attributeName,
			Observable<T> observableProperty) {
		try {
			this.bind(view, attributeName, this.resolveGetter(view.getClass(),
					attributeName), this.resolveSetter(view.getClass(),
					attributeName), observableProperty);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public <T extends View> Method resolveGetter(Class<T> viewClass,
			String attributeName) throws SecurityException,
			NoSuchMethodException {
		Log.d("Binder", "ResolveGetter: Classname: " + viewClass.getName());
		if ("android.widget.TextView".equals(viewClass.getName())) {
			if ("Text".equals(attributeName)) {
				return viewClass.getDeclaredMethod("getText");
			}
		}
		throw new NoSuchMethodException();
	}

	public <T extends View> Method resolveSetter(Class<T> viewClass,
			String attributeName) throws SecurityException,
			NoSuchMethodException {
		Log.d("Binder", "ResolveSetter: Classname: " + viewClass.getName());
		if ("android.widget.TextView".equals(viewClass.getName())) {
			if ("Text".equals(attributeName)) {
				return viewClass.getDeclaredMethod("setText",
						CharSequence.class);
			}
		}
		throw new NoSuchMethodException();
	}
	
	private HashMap<View, ViewListener> viewListeners = 
		new HashMap<View, ViewListener>();
	
	public ViewListener getViewListener(View v){
		if (viewListeners.containsKey(v)) return viewListeners.get(v);
		ViewListener vl = new ViewListener(v);
		viewListeners.put(v, vl);
		return vl;
	}
	
	public void bindCommand(View v, Class<?> listenerType, Command command) throws BindingException{
		MulticastListener<?> ml = this.getViewListener(v).getMulticastListener(listenerType);
		if (ml!=null)
			ml.register(command);
		else
			throw new BindingException("Multicast listener is not registered"); 
	}
}