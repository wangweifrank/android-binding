package gueei.binding.converters;

import gueei.binding.DependentObservable;
import gueei.binding.IObservable;

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
