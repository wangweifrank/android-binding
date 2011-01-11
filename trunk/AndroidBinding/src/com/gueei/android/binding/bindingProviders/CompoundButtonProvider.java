package com.gueei.android.binding.bindingProviders;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.R;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnCheckedChangeListenerMulticast;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.viewAttributes.GenericViewAttribute;

public class CompoundButtonProvider extends BindingProvider {

	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, int attributeId) {
		if (!(view instanceof CompoundButton)) return null;
		try{
			if (attributeId == R.id.attribute_checked){
				// TODO: Can change to very specific class to avoid the reflection methods
				ViewAttribute<CompoundButton, Boolean> attr = new GenericViewAttribute<CompoundButton, Boolean>(
						(CompoundButton)view,
						"checked", 
						CompoundButton.class.getMethod("isChecked"),
						CompoundButton.class.getMethod("setChecked", boolean.class));
				Binder.getMulticastListenerForView(view, OnCheckedChangeListenerMulticast.class)
					.register(attr);
				return (ViewAttribute<Tv, ?>)attr;
			}
		}
		catch(Exception e){
			// Actually it should never reach this statement
		}
		return null;
	}

	@Override
	public boolean bindCommand(View view, int attrId, Command command) {
		if (attrId == R.id.command_checkedChange){
			Binder.getMulticastListenerForView(view, OnCheckedChangeListenerMulticast.class).register(command);
			return true;
		}
		return false;
	}

	@Override
	public void mapBindings(View view, Context context, AttributeSet attrs,
			BindingMap map) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BindableCompoundButtons);
		String checked = a.getString(R.styleable.BindableCompoundButtons_checked);
		if (checked != null)
			map.attributes.put(R.id.attribute_checked, checked);
		String checkChanged = a.getString(R.styleable.BindableCompoundButtons_checkedChange);
		if (checkChanged!=null)
			map.commands.put(R.id.command_checkedChange, checkChanged);
	}
}
