package com.gueei.android.binding;

import java.util.AbstractCollection;
import java.util.ArrayList;

public class Observable<T> implements IObservable<T> {
	private ArrayList<Observer> observers = new ArrayList<Observer>(1);
	private T mValue;
	
	public Observable(){
	}
	
	public Observable(T initValue){
		mValue = initValue;
	}
	
	/* (non-Javadoc)
	 * @see com.gueei.android.binding.IObservable#subscribe(com.gueei.android.binding.Observer)
	 */
	public final void subscribe(Observer o){
		observers.add(o);
	}
	
	/* (non-Javadoc)
	 * @see com.gueei.android.binding.IObservable#unsubscribe(com.gueei.android.binding.Observer)
	 */
	public final void unsubscribe(Observer o){
		observers.remove(o);
	}
	
	/* (non-Javadoc)
	 * @see com.gueei.android.binding.IObservable#notifyChanged(java.lang.Object)
	 */
	public final void notifyChanged(Object initiator){
		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(initiator);
		this.notifyChanged(initiators);
	}
	
	/* (non-Javadoc)
	 * @see com.gueei.android.binding.IObservable#notifyChanged(java.util.AbstractCollection)
	 */
	public final void notifyChanged(AbstractCollection<Object> initiators){
		initiators.add(this);
		for(Observer o: observers){
			if (initiators.contains(o)) continue;
			o.onPropertyChanged(this, initiators);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.gueei.android.binding.IObservable#notifyChanged()
	 */
	public final void notifyChanged(){
		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(this);
		notifyChanged(initiators);
	}

	/* (non-Javadoc)
	 * @see com.gueei.android.binding.IObservable#set(T, java.util.AbstractCollection)
	 */
	public final void set(T newValue, AbstractCollection<Object> initiators){
		if (initiators.contains(this)) return;
		doSetValue(newValue, initiators);
		initiators.add(this);
		notifyChanged(initiators);
	}
	
	/* (non-Javadoc)
	 * @see com.gueei.android.binding.IObservable#set(T)
	 */
	public final void set(T newValue){
		doSetValue(newValue, new ArrayList<Object>());
		notifyChanged(mValue);
	}
	
	protected void doSetValue(T newValue, AbstractCollection<Object> initiators){
		try{
			mValue = (T) newValue;
		}
		catch(Exception e){}
	}
	
	protected final void setWithoutNotify(T newValue){
		mValue = newValue;
	}
	
	/* (non-Javadoc)
	 * @see com.gueei.android.binding.IObservable#get()
	 */
	public T get(){
		return mValue;
	}
}
