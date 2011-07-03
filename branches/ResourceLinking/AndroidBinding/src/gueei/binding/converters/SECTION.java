package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.collections.SingletonAdapter;
import gueei.binding.viewAttributes.templates.LayoutTemplate;
import android.widget.Adapter;

/**
 * SECTION
 * @author andy
 *
 */
public class SECTION extends Converter<Adapter> {
	public SECTION(IObservable<?>[] dependents) {
		super(Adapter.class, dependents);
	}

	@Override
	public Adapter calculateValue(Object... args) throws Exception {
		return new SingletonAdapter(this.getContext(), args[0], (LayoutTemplate)args[1], (LayoutTemplate)args[1]);
	}
}
