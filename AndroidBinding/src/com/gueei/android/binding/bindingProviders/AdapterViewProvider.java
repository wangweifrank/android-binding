package com.gueei.android.binding.bindingProviders;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.R;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnItemSelectedListenerMulticast;
import com.gueei.android.binding.listeners.TextWatcherMulticast;
import com.gueei.android.binding.viewAttributes.GenericViewAttribute;
import com.gueei.android.binding.viewAttributes.TextViewAttribute;

public class AdapterViewProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, int attributeId) {
		if (!(view instanceof AdapterView))
			return null;
		try {
			if (attributeId == R.id.attribute_adapter) {
				// TODO: Can change to very specific class to avoid the
				// reflection methods
				ViewAttribute<AdapterView, Adapter> attr = new GenericViewAttribute(
						(AdapterView) view, "adapter", 
						AdapterView.class.getMethod("getAdapter"), 
						AdapterView.class.getMethod("setAdapter", Adapter.class));
				return (ViewAttribute<Tv, ?>) attr;
			} else if (attributeId == R.id.attribute_selectedItem) {
				ViewAttribute<AdapterView, Object> attr = new GenericViewAttribute(
						(AdapterView) view, "selectedItem", AdapterView.class
								.getMethod("getSelectedItem"), null);
				attr.setReadonly(true);
				Binder.getMulticastListenerForView
					(view, OnItemSelectedListenerMulticast.class).register(attr);
				return (ViewAttribute<Tv, ?>) attr;
			}
		} catch (Exception e) {
			// Actually it should never reach this statement
		}
		return null;
	}

	@Override
	public boolean bindCommand(View view, int attrId, Command command) {
		return false;
	}

	@Override
	public void mapBindings(View view, Context context, AttributeSet attrs,
			BindingMap map) {
		if (!(view instanceof AdapterView<?>))
			return;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.BindableAdapterViews);
		String adapter = a.getString(R.styleable.BindableAdapterViews_adapter);
		if (adapter != null)
			map.attributes.put(R.id.attribute_adapter, adapter);
		String selectedItem = a
				.getString(R.styleable.BindableAdapterViews_selectedItem);
		if (selectedItem != null)
			map.attributes.put(R.id.attribute_selectedItem, selectedItem);
	}
}
