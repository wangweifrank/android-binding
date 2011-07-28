package gueei.binding.converters;

import gueei.binding.BindingSyntaxResolver;
import gueei.binding.Converter;
import gueei.binding.IObservable;

public class PROP extends Converter<Object> {

	public PROP(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... args) throws Exception {
		if (args.length<2) return null;
		if (args[0] == null) return null;
		IObservable<?> childObs = 
				BindingSyntaxResolver
					.constructObservableFromStatement(this.getContext(), args[1].toString(), args[0]);
		if (childObs!=null)
			return childObs.get();
		
		return null;
	}
}
