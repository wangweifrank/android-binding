package com.gueei.android.binding.viewFactories;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.gueei.android.binding.BindedView;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.converters.AttributePropertyBridge;
import com.gueei.android.binding.listeners.OnItemSelectedListenerMulticast;
import com.gueei.android.binding.listeners.TextWatcherMulticast;

public class AdapterViews extends BindingViewFactory {

	public void Init() {
	}

	@SuppressWarnings("unchecked")
	public boolean BindView(BindedView view, Binder binder, ViewFactory.AttributeMap attrs,
			Object model) {
		try {
			if (!(view.getView() instanceof AdapterView)) return false;
			if (attrs.containsKey("adapter")){
				Field f = model.getClass().getField(attrs.get("adapter"));
				Observable<Adapter> prop = (Observable<Adapter>) f.get(model);
				view.PutConverter("adapter", 
						new AttributePropertyBridge((ViewAttribute<Adapter>)view.getAttribute("adapter"), prop));
				prop.notifyChanged();
			}
			if (attrs.containsKey("selectedItem")){
				Field f = model.getClass().getField(attrs.get("selectedItem"));
				Observable<Object> prop = (Observable<Object>) f.get(model);
				view.PutConverter("selectedItem", 
						new AttributePropertyBridge((ViewAttribute<Adapter>)view.getAttribute("selectedItem"), prop));				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}

	public View CreateView(String name, Context context, AttributeSet attrs) {
		return null;
	}

	public void CreateViewAttributes(BindedView view, Binder binder) {
		if (!(view.getView() instanceof AdapterView)) return;
		try{
			ViewAttribute<Adapter> attr = view.addAttribute("adapter", 
					AdapterView.class.getMethod("getAdapter"),
					AdapterView.class.getMethod("setAdapter", Adapter.class),
					Adapter.class);
			
			ViewAttribute<Object> selected = view.addAttribute("selectedItem", 
					AdapterView.class.getMethod("getSelectedItem"),
					null, Object.class);
			binder.bindCommand(view.getView(), OnItemSelectedListenerMulticast.class, selected);
		}
		catch(Exception e){}
	}
}