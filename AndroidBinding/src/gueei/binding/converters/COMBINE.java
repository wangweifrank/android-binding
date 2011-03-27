package gueei.binding.converters;

import gueei.binding.DependentObservable;
import gueei.binding.IObservable;
import android.widget.Adapter;


public class COMBINE extends DependentObservable<Adapter>{
	public COMBINE(IObservable<?>[] dependents) {
		super(Adapter.class, dependents);
	}

	@Override
	public Adapter calculateValue(Object... args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
