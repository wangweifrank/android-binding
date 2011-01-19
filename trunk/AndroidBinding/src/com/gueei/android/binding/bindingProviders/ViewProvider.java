package com.gueei.android.binding.bindingProviders;

import android.view.View;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Utility;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.viewAttributes.GenericViewAttribute;
import com.gueei.android.binding.viewAttributes.VisibilityViewAttribute;

public class ViewProvider extends BindingProvider {

	@Override
	public ViewAttribute<View, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof View)) return null;
		try{
			if (attributeId.equals("enabled")){
				// TODO: Can change to very specific class to avoid the reflection methods
				ViewAttribute<View, Boolean> attr = new GenericViewAttribute<View, Boolean>(
						view,
						"enabled", 
						View.class.getMethod("isEnabled"),
						View.class.getMethod("setEnabled", boolean.class));
				return attr;
			}
			else if (attributeId.equals("visibility")){
				ViewAttribute<View, Integer> attr = 
					new VisibilityViewAttribute(view, "visibility");
				return attr;
			}
		}
		catch(Exception e){
			// Actually it should never reach this statement
		}
		return null;
	}


	@Override
	public void bind(View view, BindingMap map, Object model) {
		bindViewAttribute(view, map, model, "enabled");
		bindViewAttribute(view, map, model, "visibility");
		if (map.containsKey("click")){
			Command command = Utility.getCommandForModel(map.get("click"), model);
			if (command!=null){
				Binder
					.getMulticastListenerForView(view, OnClickListenerMulticast.class)
					.register(command);
			}
		}
	}
}
