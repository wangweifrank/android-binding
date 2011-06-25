package gueei.binding.bindingProviders;

import gueei.binding.BindingMap;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnClickListenerMulticast;
import gueei.binding.listeners.OnLongClickListenerMulticast;
import gueei.binding.viewAttributes.BackgroundColorViewAttribute;
import gueei.binding.viewAttributes.BackgroundViewAttribute;
import gueei.binding.viewAttributes.EnabledViewAttribute;
import gueei.binding.viewAttributes.VisibilityViewAttribute;
import android.view.View;


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
		else if (attributeId.equals("background")){
			ViewAttribute<View, Object> attr = 
				new BackgroundViewAttribute(view);
			return attr;
		}
		else if (attributeId.equals("backgroundColor")){
			ViewAttribute<View, Integer> attr = 
				new BackgroundColorViewAttribute(view);
			return attr;
		}
		return null;
	}


	@Override
	public void bind(View view, BindingMap map, Object model) {
		bindViewAttribute(view, map, model, "enabled");
		bindViewAttribute(view, map, model, "visibility");
		bindViewAttribute(view, map, model, "background");
		bindViewAttribute(view, map, model, "backgroundColor");
		bindCommand(view, map, model, "onClick", OnClickListenerMulticast.class);
		bindCommand(view, map, model, "onLongClick", OnLongClickListenerMulticast.class);
	}
}
