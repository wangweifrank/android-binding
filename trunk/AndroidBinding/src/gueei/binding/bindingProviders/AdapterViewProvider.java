package gueei.binding.bindingProviders;

import gueei.binding.Binder;
import gueei.binding.BindingMap;
import gueei.binding.Command;
import gueei.binding.IBindableView.AttributeHandlingMethod;
import gueei.binding.Utility;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemClickListenerMulticast;
import gueei.binding.listeners.OnItemSelectedListenerMulticast;
import gueei.binding.viewAttributes.ClickedIdViewAttribute;
import gueei.binding.viewAttributes.ClickedItemViewAttribute;
import gueei.binding.viewAttributes.ExpandableListView_ItemSourceViewAttribute;
import gueei.binding.viewAttributes.GenericViewAttribute;
import gueei.binding.viewAttributes.ItemSourceViewAttribute;
import gueei.binding.viewAttributes.ItemTemplateViewAttribute;
import gueei.binding.viewAttributes.SelectedItemViewAttribute;
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
		bindViewAttribute(view, map, model, "selectedItem");
		bindViewAttribute(view, map, model, "clickedItem");
		bindViewAttribute(view, map, model, "clickedId");
		bindViewAttribute(view, map, model, "adapter");
		bindViewAttribute(view, map, model, "itemSource");
		
		if (map.containsKey("onItemSelected")){
			Command command = Utility.getCommandForModel(map.get("onItemSelected"), model);
			if (command!=null){
				Binder
					.getMulticastListenerForView(view, OnItemSelectedListenerMulticast.class)
					.register(command);
			}
		}
		if (map.containsKey("onItemClicked")){
			Command command = Utility.getCommandForModel(map.get("onItemClicked"), model);
			if (command!=null){
				Binder
					.getMulticastListenerForView(view, OnItemClickListenerMulticast.class)
					.register(command);
			}
		}
	}
}
