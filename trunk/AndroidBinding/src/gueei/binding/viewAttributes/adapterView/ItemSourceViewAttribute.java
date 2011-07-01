package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.BindingMap;
import gueei.binding.BindingType;
import gueei.binding.Utility;
import gueei.binding.ViewAttribute;
import gueei.binding.cursor.CursorRowTypeMap;
import gueei.binding.viewAttributes.templates.LayoutTemplate;
import android.widget.Adapter;
import android.widget.AdapterView;


public class ItemSourceViewAttribute extends ViewAttribute<AdapterView<Adapter>, Object> {

	public ItemSourceViewAttribute(AdapterView<Adapter> view, String attributeName) {
		super(Object.class,view, attributeName);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null)
			return;
		
		if (newValue instanceof Adapter){
			getView().setAdapter((Adapter)newValue);
			return;
		}
		
		BindingMap map = Binder.getBindingMapForView(getView());
/*		if (!map.containsKey("itemTemplate"))
			return;
		
		int itemTemplate = Utility.resolveResource(map.get("itemTemplate"),
				Binder.getApplication());
		if (itemTemplate <= 0)
			return;
*/
		LayoutTemplate template;
		try{
			ViewAttribute<?,?> attr = Binder.getAttributeForView(getView(), "itemTemplate");
			template = ((LayoutTemplate)attr.get());
		}catch(Exception e){
			return;
		}
		int itemTemplate = template.getTemplate();
		
		int spinnerTemplate = -1;
		if (map.containsKey("spinnerTemplate")){
			spinnerTemplate = Utility.resolveResource(map.get("spinnerTemplate"),
				Binder.getApplication());
		}
		
		spinnerTemplate = spinnerTemplate >0 ? spinnerTemplate : itemTemplate;
		try {
			Adapter adapter = gueei.binding.collections.Utility.getSimpleAdapter
				(getView().getContext(), newValue, spinnerTemplate, itemTemplate);
			this.getView().setAdapter(adapter);
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
