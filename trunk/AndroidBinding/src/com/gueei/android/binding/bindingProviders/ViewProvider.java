package com.gueei.android.binding.bindingProviders;

import android.view.View;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Utility;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.viewAttributes.GenericViewAttribute;

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
				ViewAttribute<View, Integer> attr = new GenericViewAttribute<View, Integer>(
						view, "visibility",
						View.class.getMethod("getVisibility"),
						View.class.getMethod("setVisibility", int.class));
				return attr;
			}
		}
		catch(Exception e){
			// Actually it should never reach this statement
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean bind(View view, String attrName, String attrValue,
			Object model) {
		if (attrName.equals("enabled")){
			bindAttributeWithObservable(view, attrName, attrValue, model);
			return true;
		}else if (attrName.equals("visibility")){
			bindAttributeWithObservable(view, attrName, attrValue, model);
			return true;		
		}else if (attrName.equals("click")){
			Command command = Utility.getCommandForModel(attrValue, model);
			if (command!=null){
				Binder
					.getMulticastListenerForView(view, OnClickListenerMulticast.class)
					.register(command);
			}
		}
		return false;
	}
}
