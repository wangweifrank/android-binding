package com.gueei.android.binding.observables;

import com.gueei.android.binding.Observable;

public class LongObservable extends Observable<Long> {
	public LongObservable() {
		super(Long.class);
	}
	
	public LongObservable(long value){
		super(Long.class, value);
	}
}
