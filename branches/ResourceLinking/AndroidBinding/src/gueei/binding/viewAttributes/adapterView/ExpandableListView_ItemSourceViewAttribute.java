package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.BindingMap;
import gueei.binding.BindingType;
import gueei.binding.Utility;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ExpandableCollectionAdapter;
import gueei.binding.cursor.CursorRowTypeMap;
import android.widget.Adapter;
import android.widget.ExpandableListView;


public class ExpandableListView_ItemSourceViewAttribute 
	extends ViewAttribute<ExpandableListView, Object> {
	
	public  ExpandableListView_ItemSourceViewAttribute 
		(ExpandableListView view) {
		super(Object.class,view, "itemSource");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null)
			return;
		
		BindingMap map = Binder.getBindingMapForView(getView());
		if (!map.containsKey("itemTemplate"))
			return;
		
		if (!map.containsKey("childItemTemplate"))
			return;
		
		if (!map.containsKey("childItemSource"))
			return;
		
		int itemTemplate = Utility.resolveLayoutResource(map.get("itemTemplate"),
				getView().getContext());
		int childItemTemplate = Utility.resolveLayoutResource(map.get("childItemTemplate"), 
				getView().getContext());
		String childItemSource = map.get("childItemSource");
		
		if ((itemTemplate <= 0)||(childItemTemplate<=0))
			return;
		
		int spinnerTemplate = -1;
		if (map.containsKey("spinnerTemplate")){
			spinnerTemplate = Utility.resolveLayoutResource(map.get("spinnerTemplate"),
				Binder.getApplication());
		}
		
		spinnerTemplate = spinnerTemplate >0 ? spinnerTemplate : itemTemplate;
		try {
			/*
			Adapter groupAdapter = 
				gueei.binding.collections.Utility.getSimpleAdapter
					(getView().getContext(), newValue, spinnerTemplate, itemTemplate);
			ExpandableCollectionAdapter adapter = new ExpandableCollectionAdapter
				(getView().getContext(), groupAdapter, childItemSource, childItemTemplate);
			getView().setAdapter(adapter);
			*/
			
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
