package com.gueei.android.binding;

import java.util.AbstractCollection;
import java.util.ArrayList;

public abstract class DependentObservable<T> extends Observable<T> implements Observer{

	protected Observable[] dependents;
	
	public DependentObservable(Observable... dependents) {
		super();
		for(Observable<?> o : dependents){
			o.subscribe(this);
		}
		this.dependents = dependents;
		this.onPropertyChanged(null, new ArrayList<Object>());
	}

	public abstract T calculateValue(Object... args);
	
	public final <To> void onPropertyChanged(Observable<To> prop,
			AbstractCollection<Object> initiators) {
		dirty = true;
		initiators.add(this);
		this.notifyChanged(initiators);
	}

	private boolean dirty = false;
	
	@Override
	public T get() {
		if (dirty){
			int len = dependents.length;
			Object[] values = new Object[len];
			for(int i=0; i<len; i++){
				values[i] = dependents[i].get();
			}
			T value = this.calculateValue(values);
			this.setWithoutNotify(value);
			dirty = false;
		}
		return super.get();
	}
}