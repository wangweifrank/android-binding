package com.gueei.android.binding.bindingProviders;

import android.view.View;

import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.listeners.OnLongClickListenerMulticast;
import com.gueei.android.binding.viewAttributes.EnabledViewAttribute;
import com.gueei.android.binding.viewAttributes.VisibilityViewAttribute;

public class ViewProvider extends BindingProvider {

	@Override
	public ViewAttribute<View, ?> createAttributeForView(View view, String attributeId) {
		if (attributeId.equals("enabled")){
			return new EnabledViewAttribute(view, "enabled");
		}
		else if (attributeId.equals("visibility")){
			ViewAttribute<View, Integer> attr = 
				new VisibilityViewAttribute(view, "visibility");
			return attr;
		}
		return null;
	}


	@Override
	public void bind(View view, BindingMap map, Object model) {
		bindViewAttribute(view, map, model, "enabled");
		bindViewAttribute(view, map, model, "visibility");
		bindCommand(view, map, model, "onClick", OnClickListenerMulticast.class);
		bindCommand(view, map, model, "onLongClick", OnLongClickListenerMulticast.class);
	}
}
