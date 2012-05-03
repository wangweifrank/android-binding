package gueei.binding.converters;

import gueei.binding.AttributeInjector;
import gueei.binding.AttributeInjector.InjectParams;
import gueei.binding.Binder.InflateResult;
import gueei.binding.Converter;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.viewAttributes.templates.Layout;

import java.util.ArrayList;

public class INJECT extends Converter<Layout> {
	public INJECT(IObservable<?>[] dependents) {
		super(Layout.class, dependents);
	}

	@Override
	public Layout calculateValue(Object... args) throws Exception {
		if (args.length<1 || !(args[0] instanceof Layout)) return null;
		if (args.length<2) return (Layout)args[0];
		return new InjectLayout((Layout)args[0], process(args));
	}
	
	
	private static InjectParams[] process(Object[] args){
		ArrayList<InjectParams> injectParams = new ArrayList<InjectParams>();
		int len = args.length;
		if (args.length<2) return new InjectParams[0];
		for(int i=1; i<len; i++){
			if (!(args[i] instanceof DynamicObject)) continue;
			try{
				DynamicObject p = (DynamicObject)args[i];
				InjectParams param = new InjectParams();
				param.Id = (Integer)p.getObservableByName("id").get();
				param.AttrName = p.getObservableByName("attr").get().toString();
				param.Statement = p.getObservableByName("statement").get().toString();
				injectParams.add(param);
			}catch(Exception e){ continue; }
		}
		return injectParams.toArray(new InjectParams[0]);
	}
	
	public static class InjectLayout extends Layout{
		private final Layout mLayout;
		private final InjectParams[] mParams;
		
		@Override
		public void onAfterInflate(InflateResult result) {
			super.onAfterInflate(result);
			AttributeInjector.inject(mParams, result);
		}

		public InjectLayout(Layout layout, InjectParams[] params) {
			super(layout.getDefaultLayoutId());
			mLayout = layout;
			mParams = params;
		}

		@Override
		public int getLayoutTypeId(int pos) {
			return mLayout.getLayoutTypeId(pos);
		}

		@Override
		public int getLayoutId(int pos) {
			return mLayout.getLayoutId(pos);
		}

		@Override
		public int getTemplateCount() {
			return mLayout.getTemplateCount();
		}
		
	}
}
