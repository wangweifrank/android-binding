package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;

public class ROW_CHILD extends Converter<Object> {

	public ROW_CHILD(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public RowChild calculateValue(Object... args) throws Exception {		
		if (args.length<2) return null;
		
		RowChild rowChild = new RowChild();	
		
		if(args[0] != null ) {
			rowChild.childDataSource = args[0].toString();
		}
		
		if( args[1] instanceof SingleTemplateLayout) {
			rowChild.staticLayoutId = ((SingleTemplateLayout)args[1]).getDefaultLayoutId();
		} else if (args[1] != null) {
			rowChild.layoutIdName = args[1].toString();			
		}
		
		return rowChild;
	}
	
	public static class RowChild {
		public String childDataSource = null;
		public String layoutIdName = null;
		public int staticLayoutId = -1;
	}
}
