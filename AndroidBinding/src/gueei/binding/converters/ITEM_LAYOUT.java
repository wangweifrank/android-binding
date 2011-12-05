package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;

/**
 * ITEM_LAYOUT this adapter is used for bindable linear layouts
 *
 */
public class ITEM_LAYOUT extends Converter<Object> {

	public ITEM_LAYOUT(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public ItemLayout calculateValue(Object... args) throws Exception {		
		if (args.length<1) return null;
		
		ItemLayout layout = new ItemLayout();		
		if( args[0] instanceof SingleTemplateLayout) {
			layout.staticLayoutId = ((SingleTemplateLayout)args[0]).getDefaultLayoutId();
		} else if (args[0] != null) {
			layout.layoutIdName = args[0].toString();			
		}
		
		return layout;
	}
	
	public static class ItemLayout {
		public String layoutIdName = null;
		public int staticLayoutId = -1;
	}
}
