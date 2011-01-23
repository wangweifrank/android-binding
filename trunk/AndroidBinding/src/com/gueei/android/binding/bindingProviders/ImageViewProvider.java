package com.gueei.android.binding.bindingProviders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.viewAttributes.GenericViewAttribute;

public class ImageViewProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof ImageView)) return null;
		try{
			if (attributeId.equals("srcDrawable")){
				// TODO: Can change to very specific class to avoid the reflection methods
				ViewAttribute<ImageView, Drawable> attr = new 
					GenericViewAttribute<ImageView, Drawable>(Drawable.class, (ImageView)view, "srcDrawable",
							ImageView.class.getMethod("getDrawable"),
							ImageView.class.getMethod("setImageDrawable", Drawable.class));
				return (ViewAttribute<Tv, ?>) attr;
			}
		}
		catch(Exception e){
			// Actually it should never reach this statement
		}
		return null;
	}

	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof ImageView)) return;
		bindViewAttribute(view, map, model, "srcDrawable");
	}
}
