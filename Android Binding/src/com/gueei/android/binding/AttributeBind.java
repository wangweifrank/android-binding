package com.gueei.android.binding;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.view.View;

/* internal */ class AttributeBind<T> implements Observer{
	public WeakReference<? extends View> view;
	public String attributeName;
	public Method getter;
	public Method setter;
	public Observable<T> observable;
	
	public <V extends View> AttributeBind(V view, String attributeName, Method getter, Method setter, Observable<T> observable){
		this.view = new WeakReference<V>(view);
		this.attributeName = attributeName;
		this.getter = getter;
		this.setter = setter;
		this.observable = observable;
		observable.subscribe(this);
		this.onPropertyChanged(observable, observable.get());
	}
	
	public void onPropertyChanged(Observable<?> prop, Object newValue) {
		try {
			setter.invoke(view.get(), newValue);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
