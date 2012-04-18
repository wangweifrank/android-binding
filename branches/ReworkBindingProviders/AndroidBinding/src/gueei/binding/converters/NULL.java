package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

public class NULL extends Converter<Object> {

	public NULL(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... args) throws Exception {
		return null;
	}

}
