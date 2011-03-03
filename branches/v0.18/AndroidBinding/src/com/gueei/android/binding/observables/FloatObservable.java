package com.gueei.android.binding.observables;

public class FloatObservable extends com.gueei.android.binding.Observable<Float> {

	public FloatObservable() {
		super(Float.class);
	}

	public FloatObservable(Float initValue) {
		super(Float.class, initValue);
	}
}
