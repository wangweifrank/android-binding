package com.gueei.android.binding;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

public abstract class ViewAttribute<Tv extends View, T> extends Observable<T> implements Observer{
	
	protected WeakReference<Tv> view;
	protected String attributeName;
	private boolean readonly = false;
	
	public ViewAttribute(Tv view, String attributeName) {
		super();
		this.view = new WeakReference<Tv>(view);
		this.attributeName = attributeName;
	}
	
	@SuppressWarnings("unchecked")
	public final <To> void onPropertyChanged(Observable<To> prop, 
			AbstractCollection<Object> initiators){
		set((T)prop.get(), initiators);
	}

	protected abstract void doSetAttributeValue(Object newValue);	

	@Override
	protected void doSetValue(Object newValue, AbstractCollection<Object> initiators) {
		if (readonly) return;
		this.doSetAttributeValue(newValue);
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
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
	
	private Bridge<?> mBridge;
	public void BindTo(Observable prop){
		if (prop == null) return;
		prop.subscribe(this);
		mBridge = new Bridge(this, prop);
		this.subscribe(mBridge);
		prop.notifyChanged();
	}
	
	private static class Bridge<T> implements Observer{
		ViewAttribute<?, T> mAttribute;
		Observable<T> mBindedObservable;
		public Bridge(ViewAttribute<?, T> attribute, Observable<T> observable){
			mAttribute = attribute;
			mBindedObservable = observable;
		}
		
		@SuppressWarnings("unchecked")
		public <Ta> void onPropertyChanged(Observable<Ta> prop,
				AbstractCollection<Object> initiators) {
			if (prop!=mAttribute)return;
			mBindedObservable.set((T)prop.get(), initiators);
		}
	}
}