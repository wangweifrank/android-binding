package gueei.binding.converters;

import gueei.binding.Binder;
import gueei.binding.Converter;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.collections.Utility;
import android.widget.Adapter;

/**
 * ADAPTER accepts only one argument, and it must be DynamicObject
 * It will return a simple adapter, GIVEN that all required information is provided:
 * Parameters:
 *   1. template (required, layout id)
 *   2. source (required, any observablecollection / cursorobservable)
 *   3. spinnerTemplate (optional, layout id)
 * @author andy
 *
 */
public class ADAPTER extends Converter<Adapter> {
	public ADAPTER(IObservable<?>[] dependents) {
		super(Adapter.class, dependents);
	}

	@Override
	public Adapter calculateValue(Object... args) throws Exception {
		try{
			DynamicObject object = (DynamicObject)args[0];
			if (!object.observableExists("template")) return null;
			if (!object.observableExists("source")) return null;
			int template = (Integer)object.getObservableByName("template").get();
			int spinnerTemplate = -1;
			if (object.observableExists("spinnerTemplate"))
				spinnerTemplate = (Integer)object.getObservableByName("spinnerTemplate").get();
			spinnerTemplate = spinnerTemplate <0 ? template : spinnerTemplate;
			IObservable<?> source = object.getObservableByName("source");
			return Utility.getSimpleAdapter(Binder.getApplication(), source, template, spinnerTemplate);
		}catch(Exception e){
			return null;
		}
	}
}
