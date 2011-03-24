package com.gueei.android.binding.viewAttributes;

import android.widget.Adapter;
import android.widget.AdapterView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.BindingType;
import com.gueei.android.binding.Utility;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.cursor.CursorRowTypeMap;

public class ItemSourceViewAttribute extends ViewAttribute<AdapterView<Adapter>, Object> {

	public ItemSourceViewAttribute(AdapterView<Adapter> view, String attributeName) {
		super(Object.class,view, attributeName);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null)
			return;
		
		BindingMap map = Binder.getBindingMapForView(getView());
		if (!map.containsKey("itemTemplate"))
			return;
		
		int itemTemplate = Utility.resolveResource(map.get("itemTemplate"),
				Binder.getApplication());
		if (itemTemplate <= 0)
			return;
		
		int spinnerTemplate = -1;
		if (map.containsKey("spinnerTemplate")){
			spinnerTemplate = Utility.resolveResource(map.get("spinnerTemplate"),
				Binder.getApplication());
		}
		
		spinnerTemplate = spinnerTemplate >0 ? spinnerTemplate : itemTemplate;
		try {
			Adapter adapter = com.gueei.android.binding.collections.Utility.getSimpleAdapter
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
