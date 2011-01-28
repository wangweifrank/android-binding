package com.gueei.android.binding.observables;

import com.gueei.android.binding.Observable;

public class StringObservable extends Observable<String> {
	public StringObservable() {
		super(String.class);
	}
	
	public StringObservable(String value){
		super(String.class, value);
	}
}
