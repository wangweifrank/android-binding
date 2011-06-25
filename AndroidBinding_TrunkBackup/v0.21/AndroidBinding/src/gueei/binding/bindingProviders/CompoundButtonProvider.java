package gueei.binding.bindingProviders;

import gueei.binding.Binder;
import gueei.binding.BindingMap;
import gueei.binding.Command;
import gueei.binding.Utility;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnCheckedChangeListenerMulticast;
import gueei.binding.viewAttributes.CheckedViewAttribute;
import android.view.View;
import android.widget.CompoundButton;


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
