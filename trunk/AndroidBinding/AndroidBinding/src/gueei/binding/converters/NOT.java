package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * TwoWayDependentObservable to perform NOT operation. Only accepts one argument and boolean for two-way conversion
 * @author andy
 *
 */
public class NOT extends Converter<Boolean> {

	public NOT(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public boolean ConvertBack(Object value, Object[] outResult) {
		if ((!(value instanceof Boolean)) || (outResult.length<1)) return false;
		outResult[0] = !((Boolean)value);
		return true;
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		if (args.length<1) return true;
		if (args[0] instanceof Boolean){
			return !((Boolean)args[0]);
		}
		if (args[0] instanceof Number){
			return !args[0].equals(0);
		}
		if (args[0] != null)
			return false;
		
		return true;
	}
}
