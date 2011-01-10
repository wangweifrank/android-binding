package com.gueei.android.binding.bindingProviders;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.R;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.viewAttributes.GenericViewAttribute;

public class ImageViewProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, int attributeId) {
		if (!(view instanceof ImageView)) return null;
		try{
			if (attributeId == R.id.attribute_srcDrawable){
				// TODO: Can change to very specific class to avoid the reflection methods
				ViewAttribute<ImageView, Drawable> attr = new 
					GenericViewAttribute<ImageView, Drawable>((ImageView)view, "srcDrawable",
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
	public boolean bindCommand(View view, int attrId, Command command) {
		return false;
	}

	@Override
	public void mapBindings(View view, Context context, AttributeSet attrs,
			BindingMap map) {
		if (!(view instanceof ImageView)) return;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BindableImageViews);
		String srcDrawable = a.getString(R.styleable.BindableImageViews_srcDrawable);
		if (srcDrawable != null)
			map.attributes.put(R.id.attribute_srcDrawable, srcDrawable);
	}
}
