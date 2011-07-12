package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.BindingType;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import gueei.binding.cursor.CursorRowTypeMap;
import gueei.binding.viewAttributes.templates.Layout;

import java.util.Collection;

import android.widget.Adapter;
import android.widget.AdapterView;


public class ItemSourceViewAttribute extends ViewAttribute<AdapterView<Adapter>, Object> {

	Layout template, spinnerTemplate;
	ViewAttribute<?,Layout> itemTemplateAttr, spinnerTemplateAttr;

	private Observer templateObserver = new Observer(){
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			template = itemTemplateAttr.get();
			spinnerTemplate = spinnerTemplateAttr.get();
		}
	};
	
	@SuppressWarnings("unchecked")
	public ItemSourceViewAttribute(AdapterView<Adapter> view, String attributeName) {
		super(Object.class,view, attributeName);
		
		try{
			itemTemplateAttr = (ViewAttribute<?,Layout>)Binder.getAttributeForView(getView(), "itemTemplate");
			itemTemplateAttr.subscribe(templateObserver);
			spinnerTemplateAttr = (ViewAttribute<?,Layout>)Binder.getAttributeForView(getView(), "spinnerTemplate");
			spinnerTemplateAttr.subscribe(templateObserver);
			template = itemTemplateAttr.get();
			spinnerTemplate = spinnerTemplateAttr.get();
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null)
			return;
		
		if (newValue instanceof Adapter){
			getView().setAdapter((Adapter)newValue);
			return;
		}
		
		if (template==null) return;
		
		spinnerTemplate = spinnerTemplate == null ? template : spinnerTemplate;
		
		try {
			Adapter adapter = gueei.binding.collections.Utility.getSimpleAdapter
				(getView().getContext(), newValue, spinnerTemplate, template);
			this.getView().setAdapter(adapter);
			ViewAttribute<?,Integer> SelectedPosition = (ViewAttribute<?,Integer>)Binder.getAttributeForView(getView(), "selectedPosition");
			getView().setSelection(SelectedPosition.get());
			return;
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
		if (type.isArray()||CursorRowTypeMap.class.isAssignableFrom(type)){
			return BindingType.OneWay;
		}
		return BindingType.OneWay;
	}
}
