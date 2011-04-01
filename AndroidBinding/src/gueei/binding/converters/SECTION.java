package gueei.binding.converters;

import android.widget.Adapter;
import gueei.binding.Binder;
import gueei.binding.DependentObservable;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.collections.SingletonAdapter;
import gueei.binding.collections.Utility;

/**
 * SECTION
 * @author andy
 *
 */
public class SECTION extends DependentObservable<Adapter> {
	public SECTION(IObservable<?>[] dependents) {
		super(Adapter.class, dependents);
	}

	@Override
	public Adapter calculateValue(Object... args) throws Exception {
		return new SingletonAdapter(null, args, 0, 0);
	}
}
