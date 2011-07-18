package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.collections.Utility;
import gueei.binding.viewAttributes.templates.Layout;
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
			Layout template = ((Layout)object.getObservableByName("template").get());
			Layout spinnerTemplate = null;
			if (object.observableExists("spinnerTemplate"))
				spinnerTemplate = (Layout)object.getObservableByName("spinnerTemplate").get();
			
			spinnerTemplate = spinnerTemplate == null ? template : spinnerTemplate;
			
			IObservable<?> source = object.getObservableByName("source");
			
			return Utility.getSimpleAdapter(getContext(), source, template, spinnerTemplate);
		}catch(Exception e){
			return null;
		}
	}
}
