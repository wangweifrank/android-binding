package com.gueei.android.binding.bindingProviders;

import android.view.View;
import android.widget.RatingBar;

import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnRatingBarChangeListenerMulticast;
import com.gueei.android.binding.viewAttributes.RatingViewAttribute;

public class RatingBarProvider extends BindingProvider {
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof RatingBar)) return null;
		if (attributeId.equals("rating")){
			RatingViewAttribute attr = new RatingViewAttribute((RatingBar)view);
			return (ViewAttribute<Tv, ?>)attr;
		}
		return null;
	}


	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof RatingBar)) return;
		bindViewAttribute(view, map, model, "rating");
		bindCommand(view, map, model, "onRatingChanged", OnRatingBarChangeListenerMulticast.class);
	}
}
