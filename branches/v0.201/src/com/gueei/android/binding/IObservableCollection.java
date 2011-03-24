package com.gueei.android.binding;


@SuppressWarnings("rawtypes")
public interface IObservableCollection<T> extends IObservable<IObservableCollection>{
	void subscribe(CollectionObserver c);
	void unsubscribe(CollectionObserver c);
	void notifyCollectionChanged();
	T getItem(int position);
	void onLoad(int position);
	Class<T> getComponentType();
	int size();
}