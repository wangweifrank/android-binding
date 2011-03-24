package com.gueei.android.binding.observables;

import java.util.AbstractCollection;

import com.gueei.android.binding.Observable;

public class StringObservable extends Observable<String> {
	public StringObservable() {
		super(String.class);
	}
	
	public StringObservable(String value){
		super(String.class, value);
	}

	@Override
	public void _setObject(Object newValue,
			AbstractCollection<Object> initiators) {
		if (newValue!=null)
			this.set(newValue.toString());
	}
}
