package gueei.binding.v30.viewAttributes.adapterView.viewPager;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.templates.LayoutItem;
import android.view.View;

public class ItemLayoutTemplateViewAttribute extends ViewAttribute<View, LayoutItem> {

	public ItemLayoutTemplateViewAttribute(View view,
			String attributeName) {
		super(LayoutItem.class, view, attributeName);
	}

	private LayoutItem template;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue instanceof LayoutItem){
			template = (LayoutItem)newValue;
			return;
		}
	}

	@Override
	public LayoutItem get() {
		return template;
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (LayoutItem.class.isAssignableFrom(type)) return BindingType.TwoWay;
		return BindingType.NoBinding;
	}
}
