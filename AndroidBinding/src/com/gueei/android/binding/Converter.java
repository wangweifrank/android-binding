package com.gueei.android.binding;

import java.util.AbstractCollection;

public abstract class Converter<T> extends DependentObservable<T> {

	public Converter(Observable... dependents){
		super(dependents);
	}
	
	@Override
	public void set(T newValue) {
		super.set(newValue);
	}

	@Override
	public void set(T newValue, AbstractCollection<Object> initiators) {
		super.set(newValue, initiators);
		if (initiators.contains(this))return;
		initiators.add(this);
		int count = dependents.length;
		Object[] outResult = new Object[count]; 
		ConvertBack(newValue, outResult);
		for(int i=0; i<count; i++){
			dependents[i].set(outResult[i], initiators);
		}
	}

	public abstract void ConvertBack(T value, Object[] outResult);
}
