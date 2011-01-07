package com.gueei.android.binding;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractCollection;

import android.R;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ViewAttribute<T> extends Observable<T> implements Observer, Command{
	
	public WeakReference<? extends View> view;
	public String attributeName;
	public Method getter;
	public Method setter;
	
	@SuppressWarnings("unchecked")
	public <V extends View> ViewAttribute(V view, String attributeName, Method getter, Method setter) 
		throws Exception{
		super((T)getter.invoke(view));
		this.view = new WeakReference<V>(view);
		this.attributeName = attributeName;
		this.getter = getter;
		this.setter = setter;
	}
	
	public void viewEventRaised(){
		
	}
	
	public <T> void onPropertyChanged(Observable<T> prop, T newValue,
			AbstractCollection<Object> initiators){
		if (setter==null) return;
		try {
			if (!initiators.contains(this)){
				setter.invoke(view.get(), newValue);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void set(T newValue, AbstractCollection<Object> initiators) {
		try{
			if (initiators.contains(view.get())) return;
			setter.invoke(view.get(), newValue);
//			initiators.add(this);
//			this.notifyChanged(newValue, initiators);
		}catch(Exception e){
			
		}
	}

	@Override
	public void set(T newValue) {
		//this.set(newValue, this);
	}

	@Override
	public T get() {
		try{
			T value = (T)getter.invoke(view.get());
			return value;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void Invoke(View view, Object... args) {
		onAttributeChanged();
	}

	@SuppressWarnings("unchecked")
	public void onAttributeChanged() {
		try{
			T value = (T)getter.invoke(view.get());
			this.notifyChanged(value, view.get());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}