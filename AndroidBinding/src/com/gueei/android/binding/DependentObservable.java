package com.gueei.android.binding;

import java.util.AbstractCollection;
import java.util.ArrayList;

public abstract class DependentObservable<T> extends Observable<T> implements Observer{

	protected IObservable[] dependents;
	
	public DependentObservable(IObservable... dependents) {
		super();
		for(IObservable<?> o : dependents){
			o.subscribe(this);
		}
		this.dependents = dependents;
		this.onPropertyChanged(null, new ArrayList<Object>());
	}

	public abstract T calculateValue(Object... args);
	
	public final void onPropertyChanged(IObservable<?> prop,
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