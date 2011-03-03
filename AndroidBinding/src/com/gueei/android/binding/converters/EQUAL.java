package com.gueei.android.binding.converters;

import com.gueei.android.binding.Converter;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.IObservable;

/**
 * Converter to perform Equal operation. 
 * Accepts two arguments only and return the Equals operation on A to B
 * @author andy
 *
 */
public class EQUAL extends DependentObservable<Boolean> {

	public EQUAL(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		if (args.length<2) return false;
		return args[0].equals(args[1]);
	}
}
