package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.textView.OnTextChangedViewEvent;
import gueei.binding.viewAttributes.textView.TextColorViewAttribute;
import gueei.binding.viewAttributes.textView.TextViewAttribute;
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
			return (ViewAttribute<Tv, ?>) attr;
		}
		if (attributeId.equals("textColor")){
			TextColorViewAttribute attr = new TextColorViewAttribute((TextView)view);
			return (ViewAttribute<Tv, ?>) attr;
		}
		if (attributeId.equals("onTextChanged")){
			if (view instanceof EditText){
				return (ViewAttribute<Tv, ?>) (new OnTextChangedViewEvent((EditText)view));
			}
		}
		return null;
	}
}