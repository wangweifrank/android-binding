package com.gueei.android.binding.converters;

import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.IObservable;

public class IF extends DependentObservable<Object> {

	public IF(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	/**
	 * IF takes three arguments
	 * argument 0: Condition to test
	 * argument 1: If condition fulfilled, return argument 1
	 * argument 2: else return argument 2
	 * if the argument 0 is not boolean, argument 1 will be returned
	 */
	@Override
	public Object calculateValue(Object... args) throws Exception {
		if (args.length<3) throw new IllegalArgumentException();
		if (args[0].equals(Boolean.FALSE))
			return args[2];
		return args[1];
	}
}
