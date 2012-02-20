package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/* IF takes three arguments, 
 * Argument1: Test condition, must be Boolean
 * Argument2: Output if the condition is true
 * Argument3: Output when condition is false
 */

public class IF extends Converter<Object> {

	public IF(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... args) throws Exception {
		if (args.length<3) return null;		
		if (args[0] == null) return null;		
		if ((Boolean)args[0]){
			return args[1];
		}else{
			return args[2];
		}
	}
}
