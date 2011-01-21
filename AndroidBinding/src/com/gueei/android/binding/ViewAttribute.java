package com.gueei.android.binding;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

public abstract class ViewAttribute<Tv extends View, T> extends Observable<T> {
	
	protected WeakReference<Tv> view;
	protected String attributeName;
	private boolean readonly = false;
	
	public ViewAttribute(Tv view, String attributeName) {
		super();
		this.view = new WeakReference<Tv>(view);
		this.attributeName = attributeName;
	}
	
	@SuppressWarnings("unchecked")
	public final <To> void onPropertyChanged(IObservable<To> prop, 
			AbstractCollection<Object> initiators){
		set((T)prop.get(), initiators);
	}

	protected abstract void doSetAttributeValue(Object newValue);	

	@Override
	protected void doSetValue(T newValue, AbstractCollection<Object> initiators) {
		if (readonly) return;
		this.doSetAttributeValue(newValue);
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	private void setObject(Object newValue, AbstractCollection<Object> initiators){
		this.doSetAttributeValue(newValue);
	}
	
	@Override
	public abstract T get();

	public void onAttributeChanged(View view, Object... args) {
		try{
			T value = this.get();
			this.notifyChanged(this.view.get());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private Bridge mBridge;
	public void BindTo(IObservable prop){
		if (prop == null) return;
		mBridge = new Bridge(this, prop);
		prop.subscribe(mBridge);
		this.subscribe(mBridge);
		prop.notifyChanged();
	}
	
	private class Bridge implements Observer{
		ViewAttribute<Tv, T> mAttribute;
		IObservable<Object> mBindedObservable;
		public Bridge(ViewAttribute<Tv, T> attribute, IObservable<Object> observable){
			mAttribute = attribute;
			mBindedObservable = observable;
		}
		
		@SuppressWarnings("unchecked")
		public void onPropertyChanged(IObservable<?> prop,
				AbstractCollection<Object> initiators) {
			if (prop==mAttribute){
				mBindedObservable.set(prop.get(), initiators);
			}
			else if (prop==mBindedObservable){
				mAttribute.setObject(prop.get(), initiators);
			}
		}
	}
}