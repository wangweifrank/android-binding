package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

public class LEN extends Converter<Integer> {
	public LEN(IObservable<?>[] dependents) {
		super(Integer.class, dependents);
	}

	@Override
	public Integer calculateValue(Object... args) throws Exception {
		return args[0].toString().length();
	}
}
