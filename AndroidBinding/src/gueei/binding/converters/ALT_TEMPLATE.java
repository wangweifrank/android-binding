package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.viewAttributes.templates.Layout;

public class ALT_TEMPLATE extends Converter<Layout> {
	public ALT_TEMPLATE(IObservable<?>[] dependents) {
		super(Layout.class, dependents);
	}

	@Override
	public Layout calculateValue(Object... args) throws Exception {
		int[] ids = new int[args.length];
		for (int i=0; i<args.length; i++){
			ids[i] = ((Layout)args[i]).getDefaultLayoutId();
		}
		return new Alt_Layout(ids);
	}
	
	private static class Alt_Layout extends Layout{
		private int[] mLayouts;
		
		public Alt_Layout(int[] layouts) {
			super(layouts[0]);
			mLayouts = layouts;
		}

		@Override
		public int getLayoutTypeId(Object item, int pos) {
			return pos%mLayouts.length;
		}

		@Override
		public int getLayoutId(Object item, int pos) {
			int idx = pos % mLayouts.length;
			return mLayouts[idx];
		}

		@Override
		public int getTemplateCount() {
			return mLayouts.length;
		}
	}
}
