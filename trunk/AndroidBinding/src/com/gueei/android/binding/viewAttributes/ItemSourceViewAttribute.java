package com.gueei.android.binding.viewAttributes;

import android.database.Cursor;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.BindingType;
import com.gueei.android.binding.Utility;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.collections.ArrayAdapter;
import com.gueei.android.binding.cursor.CursorAdapter;
import com.gueei.android.binding.cursor.CursorRowTypeMap;

public class ItemSourceViewAttribute extends ViewAttribute<AdapterView<Adapter>, Object> {

	public ItemSourceViewAttribute(AdapterView<Adapter> view, String attributeName) {
		super(Object.class,view, attributeName);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null)
			return;
		if ((newValue instanceof CursorRowTypeMap)) {
			setCursorAdapter((CursorRowTypeMap) newValue);
			return;
		}
		if (newValue.getClass().isArray())
			setArrayAdapter(newValue);
	}

	@SuppressWarnings("unchecked")
	private void setArrayAdapter(Object newValue) {
		try {
			BindingMap map = Binder.getBindingMapForView(getView());
			if (!map.containsKey("itemTemplate"))
				return;
			int itemTemplate = Utility.resolveResource(map.get("itemTemplate"),
					Binder.getApplication());
			ArrayAdapter adapter = new ArrayAdapter(Binder.getApplication(),
					newValue.getClass().getComponentType(),
					(Object[]) newValue, itemTemplate);
			getView().setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void setCursorAdapter(CursorRowTypeMap rowTypeMap) {
		Cursor cursor = rowTypeMap.getCursor();
		if (cursor == null)
			return;
		if (cursor.isClosed())
			return;

		BindingMap map = Binder.getBindingMapForView(getView());
		if (!map.containsKey("itemTemplate"))
			return;

		int itemTemplate = Utility.resolveResource(map.get("itemTemplate"),
				Binder.getApplication());
		if (itemTemplate < 0)
			return;

		try {
			CursorAdapter<?> adapter = new CursorAdapter(Binder
					.getApplication(), rowTypeMap, itemTemplate);
			getView().setAdapter(adapter);
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
		return BindingType.NoBinding;
	}
}
