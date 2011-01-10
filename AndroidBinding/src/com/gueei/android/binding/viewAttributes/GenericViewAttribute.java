package com.gueei.android.binding.viewAttributes;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractCollection;

import com.gueei.android.binding.Observable;
import com.gueei.android.binding.ViewAttribute;

import android.view.View;

public class GenericViewAttribute<Tv extends View, T> extends ViewAttribute<Tv, T>{
	public Method getter;
	public Method setter;

	@SuppressWarnings("unchecked")
	public GenericViewAttribute(Tv view, String attributeName, Method getter, Method setter) 
		throws Exception{
		super(view, attributeName);
		this.getter = getter;
		this.setter = setter;
		T value = (T)this.getter.invoke(view);
		this.set(value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T get() {
		T value;
		try {
			value = (T)this.getter.invoke(view.get());
			return value;
		} catch (Exception e){
			return null;
		}
	}

	@Override
	protected void doSet(T newValue) {
		try{
			this.setter.invoke(view.get(), newValue);
		}catch(Exception e){
			
		}
	}
}
