package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * TwoWayDependentObservable to perform Equal operation. 
 * Accepts two arguments only and return the Equals operation on A to B
 * @author andy
 *
 */
public class EQUAL extends Converter<Boolean> {

	public EQUAL(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		if (args.length<2) return false;
		return args[0].equals(args[1]);
	}
}
