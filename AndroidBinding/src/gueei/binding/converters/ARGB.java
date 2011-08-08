package gueei.binding.converters;

import android.graphics.Color;
import gueei.binding.Converter;
import gueei.binding.IObservable;

/*
 * Covert to Color value of three components or four components
 * If only three arguments specified, color will be RGB
 * if four arguments provided, color will be ARGB
 * return type as Android Color integer
 */

public class ARGB extends Converter<Integer> {

	public ARGB(IObservable<?>[] dependents) {
		super(Integer.class, dependents);
	}

	@Override
	public Integer calculateValue(Object... args) throws Exception {
		if (args.length<3) return 0;
		if (args.length<4)
			return Color.rgb((Integer)args[0], (Integer)args[1], (Integer)args[2]);
		return Color.argb((Integer)args[0], (Integer)args[1], (Integer)args[2], (Integer)args[3]);
	}

}
