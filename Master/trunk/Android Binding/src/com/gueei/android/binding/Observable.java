package com.gueei.android.binding;

import java.util.AbstractCollection;
import java.util.ArrayList;

public class Observable<T> {
	private ArrayList<Observer> observers = new ArrayList<Observer>(1);
	private T mValue;
	
	public Observable(T initValue){
		mValue = initValue;
	}
	
	public final void subscribe(Observer o){
		observers.add(o);
	}
	
	public final void notifyChanged(T newValue, Object initiator){
		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(initiator);
		this.notifyChanged(newValue, initiators);
	}
	
	public final void notifyChanged(T newValue, AbstractCollection<Object> initiators){
		initiators.add(this);
		for(Observer o: observers){
			if (initiators.contains(o)) continue;
			o.onPropertyChanged(this, newValue, initiators);
		}
	}
	
	public final void notifyChanged(T newValue){
		notifyChanged(newValue, new ArrayList<Object>());
	}

	public void set(T newValue, AbstractCollection<Object> initiators){
		if (initiators.contains(this)) return;
		mValue = newValue;
		notifyChanged(mValue, initiators);
	}
	
	public void set(T newValue){
		mValue = newValue;
		notifyChanged(mValue);
	}
	
	public T get(){
		return mValue;
	}
}
