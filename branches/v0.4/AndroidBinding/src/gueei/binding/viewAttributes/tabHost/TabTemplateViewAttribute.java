package gueei.binding.viewAttributes.tabHost;

import android.view.View;
import gueei.binding.BindingType;
import gueei.binding.Utility;
import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;

/**
 * User: =ra=
 * Date: 16.08.11
 * Time: 20:30
 */
public class TabTemplateViewAttribute extends ViewAttribute<View, Layout> {

	public TabTemplateViewAttribute(View view) {
		super(Layout.class, view, "tabTemplate");
	}

	private Layout template;

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue instanceof Layout){
			template = (Layout)newValue;
			return;
		}else if (newValue instanceof Integer){
			if ((Integer)newValue>0)
				template = new SingleTemplateLayout((Integer)newValue);
			return;
		}else if (newValue instanceof CharSequence){
			int value = Utility.resolveLayoutResource(newValue.toString(), getView().getContext());
			if (value>0){
				template = new SingleTemplateLayout((Integer)newValue);
			}
			return;
		}
	}

	@Override
	public Layout get() {
		return template;
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (Integer.class.isAssignableFrom(type) ||
			CharSequence.class.isAssignableFrom(type))
			return BindingType.OneWay;
		if (Layout.class.isAssignableFrom(type)) return BindingType.TwoWay;
		return BindingType.NoBinding;
	}
}
