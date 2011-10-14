package com.gueei.demos.innerfieldobservable.converter;

import gueei.binding.Converter;
import gueei.binding.IObservable;

public class ISNULL extends Converter<Boolean> {

	public ISNULL(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		if (args[0]==null) return true;
		else return false;
	}

}
