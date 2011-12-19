package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/* IF takes three arguments, 
 * Argument1: Object - if is null the fallback object will be returned
 * Argument2: fallback object
 * Argument3: Default object
 * 
 * or
 * 
 * Argument1: Object - if is not null use this as binding
 * Argument2: fallback object
 */

public class IF_NULL extends Converter<Object> {

	public IF_NULL(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... args) throws Exception {		
		if( args.length == 2) {
			if ((Object)args[0] != null){
				return args[0];
			}else{
				return args[1];
			}
		}
		if( args.length == 3) {
			if ((Object)args[0] == null){
				return args[2];
			}else{
				return args[1];
			}		
		}
		return null;
	}
}
