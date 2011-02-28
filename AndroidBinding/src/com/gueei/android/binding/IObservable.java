package com.gueei.android.binding;

import java.util.AbstractCollection;

public interface IObservable<T> {

	// Hack to know the generic type of observable
	public abstract Class<T> getType();
	
	public abstract void subscribe(Observer o);

	public abstract void unsubscribe(Observer o);
	
	// For debug purpose, internal use only
	abstract Observer[] getAllObservers();

	public abstract void notifyChanged(Object initiator);

	public abstract void notifyChanged(AbstractCollection<Object> initiators);

	public abstract void notifyChanged();

	public abstract void set(T newValue, AbstractCollection<Object> initiators);

	public abstract void set(T newValue);
	
	/**
	 * _setObject() is a type unchecked version of set()
	 * This is intended for internal use only
	 * @param newValue
	 * @param initiators
	 */
	public abstract void _setObject(Object newValue, AbstractCollection<Object> initiators);
	
	public abstract T get();
}