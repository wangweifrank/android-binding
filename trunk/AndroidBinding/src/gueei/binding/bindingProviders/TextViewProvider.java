package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.textView.CheckedViewAttribute;
import gueei.binding.viewAttributes.textView.OnTextChangedViewEvent;
import gueei.binding.viewAttributes.textView.TextColorViewAttribute;
import gueei.binding.viewAttributes.textView.TextLinesViewAttribute;
import gueei.binding.viewAttributes.textView.TextViewAttribute;
import gueei.binding.viewAttributes.textView.TypefaceViewAttribute;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;


public class TextViewProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (view instanceof CheckedTextView) {
			if (attributeId.equals("checked")){
				CheckedViewAttribute attr = new CheckedViewAttribute((CheckedTextView)view);
				return (ViewAttribute<Tv, ?>) attr;
			}		
		}		
		if (!(view instanceof TextView)) return null;
		if (attributeId.equals("text")){
			TextViewAttribute attr = new TextViewAttribute((TextView)view, "text");
			return (ViewAttribute<Tv, ?>) attr;
		}
		if (attributeId.equals("minLines")){
			TextLinesViewAttribute attr = new TextLinesViewAttribute((TextView)view, TextLinesViewAttribute.Mode.MinLines);
			return (ViewAttribute<Tv, ?>) attr;
		}
		if (attributeId.equals("maxLines")){
			TextLinesViewAttribute attr = new TextLinesViewAttribute((TextView)view, TextLinesViewAttribute.Mode.MaxLines);
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
		if (attributeId.equals("typeface")){
			TypefaceViewAttribute attr = new TypefaceViewAttribute((TextView)view);
			return (ViewAttribute<Tv, ?>) attr;
		}
				
		
		
		return null;
	}
}