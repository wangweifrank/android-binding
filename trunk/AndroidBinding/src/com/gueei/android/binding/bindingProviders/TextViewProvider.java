package com.gueei.android.binding.bindingProviders;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.R;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.converters.AttributePropertyBridge;
import com.gueei.android.binding.exception.AttributeNotDefinedException;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;

public class TextViewProvider extends BindingProvider {

	@Override
	public ViewAttribute<?> createAttributeForView(View view, int attributeId) {
		if (!(view instanceof TextView)) return null;
		try{
			if (attributeId == R.id.attribute_text){
				// TODO: Can change to very specific class to avoid the reflection methods
				ViewAttribute<CharSequence> attr = new ViewAttribute<CharSequence>(
						view,
						"text", 
						TextView.class.getMethod("getText"),
						TextView.class.getMethod("setText", CharSequence.class));
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
		return false;
	}

	@Override
	public void mapBindings(View view, Context context, AttributeSet attrs,
			BindingMap map) {
		if (!(view instanceof TextView)) return;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BindableTextViews);
		String text = a.getString(R.styleable.BindableTextViews_text);
		if (text != null)
			map.attributes.put(R.id.attribute_text, text);
	}
}
