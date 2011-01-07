package com.gueei.android.binding;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractCollection;

import android.R;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/* internal */ class AttributeBind<T> implements Observer, Command{
	public WeakReference<? extends View> view;
	public String attributeName;
	public Method getter;
	public Method setter;
	public Observable<T> observable;
	private boolean stopPropagation = false;
	
	// Prevent loop back for the value
	private T originalValue;
	
	public <V extends View> AttributeBind(V view, String attributeName, Method getter, Method setter, Observable<T> observable){
		this.view = new WeakReference<V>(view);
		this.attributeName = attributeName;
		this.getter = getter;
		this.setter = setter;
		this.observable = observable;
		observable.subscribe(this);
		this.onPropertyChanged(observable, observable.get(), null);
		this.stopPropagation = false;
	}
	
	public void viewEventRaised(){
		
	}
	
	public <T> void onPropertyChanged(Observable<T> prop, T newValue, AbstractCollection<Object> initiators) {
		/*
		try {
			if (!this.equals(initiator)){
				setter.invoke(view.get(), newValue);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}*/
	}

	public void Invoke(View view, Object... args) {
		//if (this.view.get().equals(view)) return;
		onAttributeChanged();
	}

	@SuppressWarnings("unchecked")
	public void onAttributeChanged() {
		try{
			T value = (T)getter.invoke(view.get());
			this.observable.set(value, null);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}