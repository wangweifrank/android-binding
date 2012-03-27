package gueei.binding;


public interface CollectionObserver {
	void onCollectionChanged(IObservableCollection<?> collection, CollectionChangedEventArg args);
}
