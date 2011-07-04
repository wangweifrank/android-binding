package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ExpandableCollectionAdapter;
import gueei.binding.cursor.CursorRowTypeMap;
import gueei.binding.viewAttributes.templates.Layout;
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
		
		Layout template, childItemTemplate;
		String childItemSource;
		try{
			template = ((Layout)Binder.getAttributeForView(getView(), "itemTemplate").get());
			childItemTemplate = ((Layout)Binder.getAttributeForView(getView(), "childItemTemplate").get());
			childItemSource = (String)(Binder.getAttributeForView(getView(), "childItemSource").get());
			if (childItemSource ==null) return;
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		try {
			Adapter groupAdapter = 
				gueei.binding.collections.Utility.getSimpleAdapter
					(getView().getContext(), newValue, template, template);
			ExpandableCollectionAdapter adapter = new ExpandableCollectionAdapter
				(getView().getContext(), groupAdapter, childItemSource, childItemTemplate);
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
		return BindingType.OneWay;
	}
}
