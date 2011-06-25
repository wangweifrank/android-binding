package com.gueei.android.binding.bindingProviders;

import android.view.View;
import android.widget.ImageView;

import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.viewAttributes.ImageViewAttribute;

public class ImageViewProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof ImageView)) return null;
		if (attributeId.equals("image")){
			return 
				(ViewAttribute<Tv, ?>) new ImageViewAttribute((ImageView)view);
		}
		return null;
	}

	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof ImageView)) return;
		bindViewAttribute(view, map, model, "image");
	}
}
