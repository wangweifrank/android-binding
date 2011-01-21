package com.gueei.android.binding;

import java.util.AbstractCollection;

public interface IObservable<T> {

	public abstract void subscribe(Observer o);

	public abstract void unsubscribe(Observer o);

	public abstract void notifyChanged(Object initiator);

	public abstract void notifyChanged(AbstractCollection<Object> initiators);

	public abstract void notifyChanged();

	public abstract void set(T newValue, AbstractCollection<Object> initiators);

	public abstract void set(T newValue);

	public abstract T get();

}