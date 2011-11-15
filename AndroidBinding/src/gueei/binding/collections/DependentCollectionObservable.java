package gueei.binding.collections;

import gueei.binding.BindingLog;
import gueei.binding.CollectionObserver;
import gueei.binding.IObservableCollection;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;

public abstract class DependentCollectionObservable<T> extends Observable<T> implements CollectionObserver {

	protected IObservableCollection<?>[] mDependents;
	
	public DependentCollectionObservable(Class<T> type, IObservableCollection<?>... dependents) {
		super(type);
		for(IObservableCollection<?> o : dependents){
			o.subscribe((CollectionObserver)this);
		}
		this.mDependents = dependents;
		this.onCollectionChanged(new ArrayListObservable<Object>(null));
	}
	
	// This is provided in case the constructor can't be used. 
	// Not intended for normal usage
	public void addDependents(IObservableCollection<?>... dependents){
		IObservableCollection<?>[] temp = mDependents;
		mDependents = new IObservableCollection<?>[temp.length + dependents.length];
		int len = temp.length;
		for(int i=0; i<len; i++){
			mDependents[i] = temp[i];
		}
		int len2 = dependents.length;
		for(int i=0; i<len2; i++){
			mDependents[i+len] = dependents[i];
			dependents[i].subscribe((CollectionObserver)this);
		}
		this.onCollectionChanged(new ArrayListObservable<Object>(null));
	}
	
	public abstract T calculateValue(Object... args) throws Exception;

	@Override
	public final void onCollectionChanged(IObservableCollection<?> collection) {
		dirty = true;
		get();
	}
	
	private boolean dirty = false;	

	@Override
	public T get() {
		if (dirty){
			int len = mDependents.length;
			Object[] values = new Object[len];
			for(int i=0; i<len; i++){
				values[i] = mDependents[i].get();
			}
			try{
				T value = this.calculateValue(values);
				this.setWithoutNotify(value);
			}catch(Exception e){
				BindingLog.exception
					("DependentCollectionObservable.CalculateValue()", e);
			}
			dirty = false;
		}
		return super.get();
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}
