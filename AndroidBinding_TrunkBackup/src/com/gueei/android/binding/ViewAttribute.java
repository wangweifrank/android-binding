package com.gueei.android.binding;

import java.util.AbstractCollection;

import android.view.View;

public abstract class ViewAttribute<Tv extends View, T> extends Observable<T> {
	
	protected Tv mView;
	protected String attributeName;
	private boolean readonly = false;
	
	public ViewAttribute(Class<T> type, Tv view, String attributeName) {
		super(type);
		this.mView = view;
		this.attributeName = attributeName;
	}
	
	protected Tv getView(){
		return mView;
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

	@Override
	public void _setObject(Object newValue, AbstractCollection<Object> initiators){
		this.doSetAttributeValue(newValue);
	}
	
	@Override
	public abstract T get();

	private Bridge mBridge;
	public BindingType BindTo(IObservable<?> prop) {
		if (prop == null) return BindingType.NoBinding;
		BindingType binding = AcceptThisTypeAs(prop.getType());
		if (binding.equals(BindingType.NoBinding)) return binding;
		mBridge = new Bridge(this, prop);
		prop.subscribe(mBridge);
		if (binding.equals(BindingType.TwoWay)) this.subscribe(mBridge);
		prop.notifyChanged();
		return binding;
	}
	
	protected BindingType AcceptThisTypeAs(Class<?> type){
		if (this.getType() != type){
			if (this.getType().isAssignableFrom(type)){
				return BindingType.OneWay;
			}
			return BindingType.NoBinding;
		}
		return BindingType.TwoWay;
	}
	
	private class Bridge implements Observer{
		ViewAttribute<Tv, T> mAttribute;
		IObservable<?> mBindedObservable;
		public Bridge(ViewAttribute<Tv, T> attribute, IObservable<?> observable){
			mAttribute = attribute;
			mBindedObservable = observable;
		}
		
		public void onPropertyChanged(IObservable<?> prop,
				AbstractCollection<Object> initiators) {
			if (prop==mAttribute){
				mBindedObservable._setObject(prop.get(), initiators);
			}
			else if (prop==mBindedObservable){
				mAttribute._setObject(prop.get(), initiators);
			}
		}
	}
}