package gueei.binding.bindingProviders;

import gueei.binding.BindingMap;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.TextWatcherMulticast;
import gueei.binding.viewAttributes.TextViewAttribute;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class TextViewProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof TextView)) return null;
		if (attributeId.equals("text")){
			TextViewAttribute attr = new TextViewAttribute((TextView)view, "text");
			if (view instanceof EditText){
			}
			return (ViewAttribute<Tv, ?>) attr;
		}
		return null;
	}

	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof TextView)) return;
		bindViewAttribute(view, map, model, "text");
		if (view instanceof EditText){
			bindCommand(view, map, model, "onTextChanged", TextWatcherMulticast.class);
		}
	}
}