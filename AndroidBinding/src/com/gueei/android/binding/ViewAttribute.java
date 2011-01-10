package com.gueei.android.binding;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.ArrayList;

import android.R;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public abstract class ViewAttribute<Tv extends View, T> extends Observable<T> implements Observer, Command{
	
	protected WeakReference<Tv> view;
	protected String attributeName;
	private boolean readonly = false;
	
	public ViewAttribute(Tv view, String attributeName) {
		super();
		this.view = new WeakReference<Tv>(view);
		this.attributeName = attributeName;
	}
	
	@SuppressWarnings("unchecked")
	public <To> void onPropertyChanged(Observable<To> prop, To newValue,
			AbstractCollection<Object> initiators){
		set((T)newValue, initiators);
	}

	@Override
	public void set(T newValue, AbstractCollection<Object> initiators) {
		if (readonly) return;
		if (initiators.contains(view.get())) return;
		this.doSet(newValue);
		initiators.add(view.get());
		this.notifyChanged(newValue, initiators);
	}

	protected abstract void doSet(T newValue);
	
	@Override
	public void set(T newValue){
		if (readonly) return;
		ArrayList<Object> initiators = new ArrayList<Object>();
		set(newValue, initiators);
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	@Override
	public abstract T get();

	public void Invoke(View view, Object... args) {
		onAttributeChanged();
	}

	public void onAttributeChanged() {
		try{
			T value = this.get();
			this.notifyChanged(value, view.get());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private Bridge<?> mBridge;
	public void BindTo(Observable prop){
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
		public <Ta> void onPropertyChanged(Observable<Ta> prop, Ta newValue,
				AbstractCollection<Object> initiators) {
			if (prop!=mAttribute)return;
			mBindedObservable.set((T)newValue, initiators);
		}
	}
}