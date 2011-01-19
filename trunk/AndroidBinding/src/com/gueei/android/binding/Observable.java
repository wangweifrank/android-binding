package com.gueei.android.binding;

import java.util.AbstractCollection;
import java.util.ArrayList;

public class Observable<T> {
	private ArrayList<Observer> observers = new ArrayList<Observer>(1);
	private T mValue;
	
	public Observable(){
	}
	
	public Observable(T initValue){
		mValue = initValue;
	}
	
	public final void subscribe(Observer o){
		observers.add(o);
	}
	
	public final void unsubscribe(Observer o){
		observers.remove(o);
	}
	
	public final void notifyChanged(Object initiator){
		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(initiator);
		this.notifyChanged(initiators);
	}
	
	public final void notifyChanged(AbstractCollection<Object> initiators){
		initiators.add(this);
		for(Observer o: observers){
			if (initiators.contains(o)) continue;
			o.onPropertyChanged(this, initiators);
		}
	}
	
	public final void notifyChanged(){
		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(this);
		notifyChanged(initiators);
	}

	public final void set(Object newValue, AbstractCollection<Object> initiators){
		if (initiators.contains(this)) return;
		doSetValue(newValue, initiators);
		initiators.add(this);
		notifyChanged(initiators);
	}
	
	public final void set(Object newValue){
		doSetValue(newValue, new ArrayList<Object>());
		notifyChanged(mValue);
	}
	
	protected void doSetValue(Object newValue, AbstractCollection<Object> initiators){
		try{
			mValue = (T) newValue;
		}
		catch(Exception e){}
	}
	
	protected final void setWithoutNotify(T newValue){
		mValue = newValue;
	}
	
	public T get(){
		return mValue;
	}
}
