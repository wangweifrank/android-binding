package com.gueei.demos.markupDemo.converters;

import com.gueei.android.binding.IObservable;

public class NotEqual extends com.gueei.android.binding.converters.EQUAL {

	public NotEqual(IObservable<?>[] dependents) {
		super(dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		return !super.calculateValue(args);
	}

}
