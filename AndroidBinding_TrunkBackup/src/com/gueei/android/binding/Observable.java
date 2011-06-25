package com.gueei.android.binding;

import java.util.AbstractCollection;
import java.util.ArrayList;

public class Observable<T> implements IObservable<T> {
	private ArrayList<Observer> observers = new ArrayList<Observer>(1);
	private T mValue;
	private final Class<T> mType;
	
	public Observable(Class<T> type){
		mType = type;
	}
	
	public Observable(Class<T> type, T initValue){
		this(type);
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
	
	// Intenral use only. 
	public void _setObject(Object newValue, AbstractCollection<Object> initiators){
		T value = this.getType().cast(newValue);
		if (value==null) return;
		
		this.set(value, initiators);
	}
	
	public final void set(T newValue){
		doSetValue(newValue, new ArrayList<Object>());
		notifyChanged(mValue);
	}
	
	protected void doSetValue(T newValue, AbstractCollection<Object> initiators){
		mValue = newValue;
	}
	
	protected final void setWithoutNotify(T newValue){
		mValue = newValue;
	}
	
	public T get(){
		return mValue;
	}

	public final Class<T> getType() {
		return mType;
	}
}
