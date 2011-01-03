package com.gueei.android.binding;

import java.util.ArrayList;

public class Observable<T> {
	private ArrayList<Observer> observers = new ArrayList<Observer>(1);
	private T mValue;
	
	public Observable(T initValue){
		mValue = initValue;
	}
	
	public void subscribe(Observer o){
		observers.add(o);
	}
	
	public void notifyChanged(T newValue){
		for(Observer o: observers){
			o.onPropertyChanged(this, newValue);
		}
	}
	
	public void set(T newValue){
		if (mValue == newValue) return;
		mValue = newValue;
		notifyChanged(mValue);
	}
	
	public T get(){
		return mValue;
	}
}
