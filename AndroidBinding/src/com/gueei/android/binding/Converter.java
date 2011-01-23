package com.gueei.android.binding;

import java.util.AbstractCollection;

public abstract class Converter<T> extends DependentObservable<T> {

	public Converter(Class<T> type, IObservable<?>... dependents){
		super(type, dependents);
	}
	
	public abstract void ConvertBack(Object value, Object[] outResult);

	@Override
	protected void doSetValue(T newValue, AbstractCollection<Object> initiators) {
		int count = dependents.length;
		Object[] outResult = new Object[count];
		ConvertBack(newValue, outResult);
		for(int i=0; i<count; i++){
			dependents[i]._setObject(outResult[i], initiators);
		}
	}
}
