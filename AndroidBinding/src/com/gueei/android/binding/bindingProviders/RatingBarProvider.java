package com.gueei.android.binding.bindingProviders;

import android.view.View;
import android.widget.RatingBar;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Utility;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.listeners.OnRatingBarChangeListenerMulticast;
import com.gueei.android.binding.viewAttributes.GenericViewAttribute;
import com.gueei.android.binding.viewAttributes.VisibilityViewAttribute;

public class RatingBarProvider extends BindingProvider {
	@Override
	public ViewAttribute<View, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof RatingBar)) return null;
		try{
			if (attributeId.equals("rating")){
				// TODO: Can change to very specific class to avoid the reflection methods
				ViewAttribute<View, Float> attr = new GenericViewAttribute<View, Float>(
						view,
						"rating", 
						RatingBar.class.getMethod("getRating"),
						RatingBar.class.getMethod("setRating", float.class));
				Binder.getMulticastListenerForView(view, OnRatingBarChangeListenerMulticast.class)
					.register(attr);
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
		if (!(view instanceof RatingBar)) return;
		bindViewAttribute(view, map, model, "rating");
		/*
		if (map.containsKey("click")){
			Command command = Utility.getCommandForModel(map.get("click"), model);
			if (command!=null){
				Binder
					.getMulticastListenerForView(view, OnClickListenerMulticast.class)
					.register(command);
			}
		}
		*/
	}
}
