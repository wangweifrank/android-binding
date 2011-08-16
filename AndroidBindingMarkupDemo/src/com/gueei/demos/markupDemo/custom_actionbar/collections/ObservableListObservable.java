package com.gueei.demos.markupDemo.custom_actionbar.collections;

/**
 * User: =ra=
 * Date: 13.08.11
 * Time: 18:00
 */

import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.collections.ArrayListObservable;

import java.util.Collection;
import java.util.List;

/**
 * ObservableListObservable ia an Observable Collection of Observable items
 * collection content change even with item change causes collection change notification
 */
public class ObservableListObservable<T extends IObservable<?>> extends ArrayListObservable<T> {

	public ObservableListObservable(Class<T> type) {
		super(type);
	}

	public ObservableListObservable(Class<T> type, T[] initArray) {
		super(type, initArray);
		subscribeToItems(initArray);
	}

	@Override
	public void clear() {
		unsubscribeFromItems(mArray);
		super.clear();
	}

	@Override
	public void setArray(T[] newArray) {
		unsubscribeFromItems(mArray);
		mArray.clear();
		int size = newArray.length;
		for (T item : newArray) {
			for (int i = 0; i < size; i++) {
				mArray.add(item);
				subscribeToItem(item);
			}
		}
		this.notifyCollectionChanged();
	}

	@Override
	public void replaceItem(int position, T item) {
		T oldItem = mArray.set(position, item);
		if (null != oldItem) {
			unsubscribeFromItem(oldItem);
		}
		subscribeToItem(item);
		this.notifyCollectionChanged();
	}

	@Override public boolean add(T item) {
		boolean result = mArray.add(item);
		if (result) {
			subscribeToItem(item);
			this.notifyCollectionChanged();
		}
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		boolean result = mArray.addAll(collection);
		if (result) {
			subscribeToItems((T[]) collection.toArray());
			this.notifyCollectionChanged();
		}
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean result = mArray.removeAll(collection);
		if (result) {
			unsubscribeFromItems((T[]) collection.toArray());
			this.notifyCollectionChanged();
		}
		return result;
	}

	@Override
	public T remove(int index) {
		T removedItem = mArray.remove(index);
		if (null != removedItem) {
			unsubscribeFromItem(removedItem);
		}
		this.notifyCollectionChanged();
		return removedItem;
	}

	@Override
	public boolean remove(Object object) {
		boolean result = mArray.remove(object);
		if (result) {
			unsubscribeFromItem((T) object);
			this.notifyCollectionChanged();
		}
		return result;
	}

	@Override
	public void _setObject(Object newValue, Collection<Object> initiators) {
		if (newValue instanceof ObservableListObservable) {
			this.clear();
			this.setArray((T[]) ((ObservableListObservable<T>) newValue).toArray());
		}
	}

	@Override
	public T set(int location, T object) {
		T oldItem = mArray.set(location, object);
		subscribeToItem(object);
		if (null != oldItem) {
			unsubscribeFromItem(oldItem);
		}
		notifyCollectionChanged();
		return oldItem;
	}

	private void subscribeToItems(List<T> itemsArray) {
		for (T item : itemsArray) {
			subscribeToItem(item);
		}
	}

	private void unsubscribeFromItems(List<T> itemsArray) {
		for (T item : itemsArray) {
			unsubscribeFromItem(item);
		}
	}

	private void subscribeToItems(T[] itemsArray) {
		for (T item : itemsArray) {
			subscribeToItem(item);
		}
	}

	private void unsubscribeFromItems(T[] itemsArray) {
		for (T item : itemsArray) {
			unsubscribeFromItem(item);
		}
	}

	private void subscribeToItem(T item) {
		item.subscribe(mItemChangedObserver);
	}

	private void unsubscribeFromItem(T item) {
		item.unsubscribe(mItemChangedObserver);
	}

	private final Observer mItemChangedObserver = new Observer() {
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			notifyCollectionChanged();
		}
	};
}
