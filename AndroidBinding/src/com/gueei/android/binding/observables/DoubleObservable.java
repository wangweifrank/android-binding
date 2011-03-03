package com.gueei.android.binding.observables;

import com.gueei.android.binding.Observable;

public class DoubleObservable extends Observable<Double> {
	public DoubleObservable() {
		super(Double.class);
	}
	
	public DoubleObservable(double value){
		super(Double.class, value);
	}
}
