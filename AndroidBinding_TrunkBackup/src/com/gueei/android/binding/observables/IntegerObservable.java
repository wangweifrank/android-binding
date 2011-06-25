package com.gueei.android.binding.observables;

import com.gueei.android.binding.Observable;

public class IntegerObservable extends Observable<Integer> {
	public IntegerObservable() {
		super(Integer.class);
	}
	
	public IntegerObservable(int value){
		super(Integer.class, value);
	}
}
