package com.gueei.android.binding.bindingProviders;

import android.view.View;
import android.widget.CompoundButton;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Utility;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnCheckedChangeListenerMulticast;
import com.gueei.android.binding.viewAttributes.CheckedViewAttribute;

public class CompoundButtonProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof CompoundButton)) return null;
		if (attributeId.equals("checked")){
			ViewAttribute<CompoundButton, Boolean> attr = new CheckedViewAttribute((CompoundButton)view);
			return (ViewAttribute<Tv, ?>)attr;
		}
		return null;
	}

	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof CompoundButton)) return;
		bindViewAttribute(view, map, model, "checked");
		if (map.containsKey("onCheckedChange")){
			Command command = Utility.getCommandForModel(map.get("onCheckedChange"), model);
			if (command!=null){
				Binder
					.getMulticastListenerForView(view, OnCheckedChangeListenerMulticast.class)
					.register(command);
			}
		}
	}
}
