package gueei.binding.collections;

import gueei.binding.CollectionObserver;
import gueei.binding.IObservableCollection;
import gueei.binding.Observer;
import gueei.binding.utility.WeakList;

import java.util.Collection;


public abstract class ObservableCollection<T> implements IObservableCollection<T>{

	private WeakList<CollectionObserver> mCollectionObservers
		= new WeakList<CollectionObserver>();
	
	@SuppressWarnings("rawtypes")
	public Class<IObservableCollection> getType() {
		return IObservableCollection.class;
	}

	public final void subscribe(Observer o) {
	}

	public final void unsubscribe(Observer o) {
	}

	public final Observer[] getAllObservers() {
		// TODO Auto-generated method stub
		return null;
	}

	public final void notifyChanged(Object initiator) {
	}

	public final void notifyChanged(Collection<Object> initiators) {
	}

	public final void notifyChanged() {
	}

	@SuppressWarnings("rawtypes")
	public final void set(IObservableCollection newValue,
			Collection<Object> initiators) {
	}

	@SuppressWarnings("rawtypes")
	public final void set(IObservableCollection newValue) {
		// TODO Auto-generated method stub
		
	}

	public final void _setObject(Object newValue,
			Collection<Object> initiators) {
		// TODO Auto-generated method stub
		
	}

	public final IObservableCollection<?> get() {
		return this;
	}

	public void subscribe(CollectionObserver c) {
		mCollectionObservers.add(c);
	}

	public void unsubscribe(CollectionObserver c) {
		mCollectionObservers.remove(c);
	}

	public void notifyCollectionChanged() {
		for(CollectionObserver c: mCollectionObservers){
			c.onCollectionChanged(this);
		}
	}
}
