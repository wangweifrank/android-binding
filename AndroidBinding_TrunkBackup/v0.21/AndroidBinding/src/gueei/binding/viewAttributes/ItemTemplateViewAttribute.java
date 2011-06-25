package gueei.binding.viewAttributes;

import android.view.View;
import gueei.binding.BindingType;
import gueei.binding.Utility;
import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.templates.LayoutTemplate;
import gueei.binding.viewAttributes.templates.SingleLayoutTemplate;

public class ItemTemplateViewAttribute extends ViewAttribute<View, LayoutTemplate> {

	public ItemTemplateViewAttribute(View view,
			String attributeName) {
		super(LayoutTemplate.class, view, attributeName);
	}

	private LayoutTemplate template;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue instanceof LayoutTemplate){
			template = (LayoutTemplate)newValue;
			return;
		}else if (newValue instanceof Integer){
			if ((Integer)newValue>0)
				template = new SingleLayoutTemplate((Integer)newValue);
			return;
		}else if (newValue instanceof CharSequence){
			int value = Utility.resolveResource(newValue.toString(), getView().getContext());
			if (value>0){
				template= new SingleLayoutTemplate(value);
			}
			return;
		}
	}

	@Override
	public LayoutTemplate get() {
		return template;
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (Integer.class.isAssignableFrom(type) ||
			CharSequence.class.isAssignableFrom(type)) 
			return BindingType.OneWay;
		if (LayoutTemplate.class.isAssignableFrom(type)) return BindingType.TwoWay;
		return BindingType.NoBinding;
	}
}
