package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;

public class ITEM_PATH extends Converter<Object> {

	public ITEM_PATH(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public ItemPath calculateValue(Object... args) throws Exception {		
		if (args.length<2) return null;
		
		ItemPath path = new ItemPath();		
		if (args[0] != null) 
			path.dataSourceName = args[0].toString();
		
		if( args[1] instanceof SingleTemplateLayout) {
			path.staticLayoutId = ((SingleTemplateLayout)args[1]).getDefaultLayoutId();
		} else if (args[1] != null) {
			path.layoutIdName = args[1].toString();			
		}
		
		return path;
	}
	
	public static class ItemPath {
		public String dataSourceName = null;
		public String layoutIdName = null;
		public int staticLayoutId = -1;
	}
}
