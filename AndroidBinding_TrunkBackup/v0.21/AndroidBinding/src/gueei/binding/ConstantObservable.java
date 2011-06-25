package gueei.binding;

import java.util.AbstractCollection;

public class ConstantObservable<T> implements IObservable<T> {
	private final Class<T> mType;
	private final T mValue;
	
	public ConstantObservable(Class<T> type, T constantValue){
		mType = type;
		mValue = constantValue;
	}
	
	public Class<T> getType() {
		return mType;
	}

	public void subscribe(Observer o) {
	}

	public void unsubscribe(Observer o) {
	}

	public Observer[] getAllObservers() {
		return null;
	}

	public void notifyChanged(Object initiator) {
	}

	public void notifyChanged(AbstractCollection<Object> initiators) {
	}

	public void notifyChanged() {
	}

	public void set(T newValue, AbstractCollection<Object> initiators) {
	}

	public void set(T newValue) {
	}

	public void _setObject(Object newValue,
			AbstractCollection<Object> initiators) {
	}

	public T get() {
		return mValue;
	}
}
