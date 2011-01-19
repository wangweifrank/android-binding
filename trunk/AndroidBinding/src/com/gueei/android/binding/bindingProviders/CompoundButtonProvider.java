package com.gueei.android.binding.bindingProviders;

import android.view.View;
import android.widget.CompoundButton;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Utility;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnCheckedChangeListenerMulticast;
import com.gueei.android.binding.viewAttributes.GenericViewAttribute;

public class CompoundButtonProvider extends BindingProvider {

	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof CompoundButton)) return null;
		try{
			if (attributeId.equals("checked")){
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
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof CompoundButton)) return;
		bindViewAttribute(view, map, model, "checked");
		if (map.containsKey("checkedChange")){
			Command command = Utility.getCommandForModel(map.get("checkedChange"), model);
			if (command!=null){
				Binder
					.getMulticastListenerForView(view, OnCheckedChangeListenerMulticast.class)
					.register(command);
			}
		}
	}
}
