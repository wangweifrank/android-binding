package com.gueei.android.binding.bindingProviders;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.R;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.viewAttributes.GenericViewAttribute;

public class ViewProvider extends BindingProvider {

	@Override
	public ViewAttribute<View, ?> createAttributeForView(View view, int attributeId) {
		if (!(view instanceof View)) return null;
		try{
			if (attributeId == R.id.attribute_enabled){
				// TODO: Can change to very specific class to avoid the reflection methods
				ViewAttribute<View, Boolean> attr = new GenericViewAttribute<View, Boolean>(
						view,
						"enabled", 
						View.class.getMethod("isEnabled"),
						View.class.getMethod("setEnabled", boolean.class));
				return attr;
			}
		}
		catch(Exception e){
			// Actually it should never reach this statement
		}
		return null;
	}

	@Override
	public boolean bindCommand(View view, int attrId, Command command) {
		if (attrId == R.id.command_click){
			Binder.getMulticastListenerForView(view, OnClickListenerMulticast.class).register(command);
			return true;
		}
		return false;
	}

	@Override
	public void mapBindings(View view, Context context, AttributeSet attrs,
			BindingMap map) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BindableViews);
		String enabled = a.getString(R.styleable.BindableViews_enabled);
		if (enabled != null)
			map.attributes.put(R.id.attribute_enabled, enabled);
		String click = a.getString(R.styleable.BindableViews_click);
		if (click!=null)
			map.commands.put(R.id.command_click, click);
	}
}
