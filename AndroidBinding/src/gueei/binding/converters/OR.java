package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * TwoWayDependentObservable to perform OR operation. 
 * @author andy
 *
 */
public class OR extends Converter<Boolean> {

	public OR(IObservable<?>[] dependents) {
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

		int argLen = args.length;
		for(int i=0; i<argLen; i++){
			if (isTrue(args[i])) return true;
		}
		
		return false;
	}
	
	private boolean isTrue(Object item){
		if (item instanceof Boolean)
			 return ((Boolean)item);
		else if (item instanceof Number)
			return item.equals(0);
		else return item == null;
	}
}
