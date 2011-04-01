package gueei.binding.converters;

import gueei.binding.DependentObservable;
import gueei.binding.IObservable;
import gueei.binding.collections.CombinedAdapter;
import android.widget.Adapter;

/**
 * STITCH adapters to supply to list view
 * @author andy
 *
 */
public class STITCH extends DependentObservable<Adapter>{
	public STITCH(IObservable<?>[] dependents) {
		super(Adapter.class, dependents);
	}

	@Override
	public Adapter calculateValue(Object... args) throws Exception {
		CombinedAdapter combine = new CombinedAdapter();
		int length = args.length;
		for (int i=0; i<length; i++){
			if (args[i] instanceof Adapter){
				combine.addAdapter((Adapter)args[i]);
			}
		}
		return combine;
	}
}