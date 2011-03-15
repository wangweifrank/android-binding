package com.gueei.android.binding.observables;

import com.gueei.android.binding.Observable;

public class ObjectObservable extends Observable<Object> {
	public ObjectObservable() {
		super(Object.class);
	}
	
	public ObjectObservable(Object value){
		super(Object.class, value);
	}
}
