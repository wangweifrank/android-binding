package gueei.binding.v30.viewAttributes.adapterView.viewPager;

import gueei.binding.Binder;
import gueei.binding.BindingLog;
import gueei.binding.BindingType;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.templates.LayoutItem;

import java.util.Collection;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class ViewPager_ItemSourceViewAttribute extends ViewAttribute<ViewPager, Object> {
		
		LayoutItem template;
		
		private Observer attrObserver = new Observer(){
			public void onPropertyChanged(IObservable<?> prop,
					Collection<Object> initiators) {
				createAdapter();
			}
		};
		
		public  ViewPager_ItemSourceViewAttribute 
			(ViewPager view) {
			super(Object.class,view, "itemSource");
			try{
				ViewAttribute<?,?> attrItemLayout = Binder.getAttributeForView(getView(), "itemLayout");
				attrItemLayout.subscribe(attrObserver);
			}catch(Exception e){
				BindingLog.exception("ViewPager_ItemSourceViewAttribute", e);
			}
			createAdapter();
		}

		private void getAttributeValue() throws Exception {
			template = ((LayoutItem)Binder.getAttributeForView(getView(), "itemLayout").get());
		}

		private Object mValue;
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			mValue = newValue;
			createAdapter();
		}

		private void createAdapter(){
			if (mValue==null) return;

			try {
				getAttributeValue();
				if ((template==null))
					return;
				
				PagerAdapter pagerAdapter = 
					gueei.binding.v30.collections.UtilityV30.getSimplePagerAdapter
						(getView().getContext(), mValue, template);
				getView().setAdapter(pagerAdapter);
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
		@Override
		public Object get() {
			// Set only attribute
			return null;
		}

		@Override
		protected BindingType AcceptThisTypeAs(Class<?> type) {
			return BindingType.OneWay;
		}
}
