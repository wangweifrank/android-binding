package com.gueei.android.binding.observables;

import com.gueei.android.binding.Observable;

public class BooleanObservable extends Observable<Boolean> {
	public BooleanObservable() {
		super(Boolean.class);
	}
	
	public BooleanObservable(boolean value){
		super(Boolean.class, value);
	}
}
