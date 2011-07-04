package gueei.binding.bindingProviders;

import gueei.binding.BindingMap;
import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.GenericViewAttribute;
import gueei.binding.viewAttributes.adapterView.ChildItemSourceViewAttribute;
import gueei.binding.viewAttributes.adapterView.ClickedIdViewAttribute;
import gueei.binding.viewAttributes.adapterView.ClickedItemViewAttribute;
import gueei.binding.viewAttributes.adapterView.ExpandableListView_ItemSourceViewAttribute;
import gueei.binding.viewAttributes.adapterView.ItemSourceViewAttribute;
import gueei.binding.viewAttributes.adapterView.ItemTemplateViewAttribute;
import gueei.binding.viewAttributes.adapterView.OnItemClickedViewEvent;
import gueei.binding.viewAttributes.adapterView.OnItemSelectedViewEvent;
import gueei.binding.viewAttributes.adapterView.SelectedItemViewAttribute;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ExpandableListView;


public class AdapterViewProvider extends BindingProvider {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (!(view instanceof AdapterView))
			return null;
		try {
			if (attributeId.equals("adapter")) {
				ViewAttribute<AdapterView, Adapter> attr = new GenericViewAttribute(Adapter.class,
						(AdapterView) view, "adapter", 
						AdapterView.class.getMethod("getAdapter"), 
						AdapterView.class.getMethod("setAdapter", Adapter.class));
				return (ViewAttribute<Tv, ?>) attr;
			} else if (attributeId.equals("selectedItem")) {
				return (ViewAttribute<Tv, ?>) new SelectedItemViewAttribute((AdapterView)view, "selectedItem");
			} else if (attributeId.equals("clickedItem")){
				ViewAttribute<AdapterView<?>, Object> attr = 
					new ClickedItemViewAttribute((AdapterView)view, "clickedItem");
				return (ViewAttribute<Tv, ?>) attr;
			} else if (attributeId.equals("clickedId")){
				ViewAttribute<AdapterView<?>, Long> attr = 
					new ClickedIdViewAttribute((AdapterView)view, "clickedId");
				return (ViewAttribute<Tv, ?>) attr;
			} else if (attributeId.equals("itemSource")){
				if (view instanceof ExpandableListView){
					return (ViewAttribute<Tv, ?>)
						new ExpandableListView_ItemSourceViewAttribute((ExpandableListView)view);
				}
				ItemSourceViewAttribute attr = 
					new ItemSourceViewAttribute((AdapterView)view, "itemSource");
				return (ViewAttribute<Tv, ?>) attr;
			} else if (attributeId.equals("itemTemplate")){
				return (ViewAttribute<Tv, ?>)new ItemTemplateViewAttribute(view, "itemTemplate");
			} else if (attributeId.equals("spinnerTemplate")){
				return (ViewAttribute<Tv, ?>)new ItemTemplateViewAttribute(view, "spinnerTemplate");
			} else if (attributeId.equals("onItemSelected")){
				return (ViewAttribute<Tv, ?>)new OnItemSelectedViewEvent((AdapterView)view);
			} else if (attributeId.equals("onItemClicked")){
				return (ViewAttribute<Tv, ?>)new OnItemClickedViewEvent((AdapterView)view);
			} else if (attributeId.equals("childItemTemplate")){
				if (view instanceof ExpandableListView)
					return (ViewAttribute<Tv, ?>)new ItemTemplateViewAttribute(view, "childItemTemplate");
			} else if (attributeId.equals("childItemSource")){
				if (view instanceof ExpandableListView)
					return (ViewAttribute<Tv, ?>)new ChildItemSourceViewAttribute((ExpandableListView)view);
			}
		} catch (Exception e) {
			// Actually it should never reach this statement
		}
		return null;
	}

	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof AdapterView<?>)) return;
		bindViewAttribute(view, map, model, "itemTemplate");
		bindViewAttribute(view, map, model, "spinnerTemplate");
		bindViewAttribute(view, map, model, "childItemTemplate");
		bindViewAttribute(view, map, model, "selectedItem");
		bindViewAttribute(view, map, model, "clickedItem");
		bindViewAttribute(view, map, model, "clickedId");
		bindViewAttribute(view, map, model, "adapter");
		bindViewAttribute(view, map, model, "childItemSource");
		bindViewAttribute(view, map, model, "itemSource");
		bindViewAttribute(view, map, model, "onItemSelected");
		bindViewAttribute(view, map, model, "onItemClicked");
	}
}
