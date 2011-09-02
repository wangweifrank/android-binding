package gueei.binding.viewAttributes.view;

import gueei.binding.ViewAttribute;
import android.view.View;

public class EnabledViewAttribute extends ViewAttribute<View, Boolean> {

	public EnabledViewAttribute(View view, String attributeName) {
		super(Boolean.class, view, attributeName);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue==null){
			getView().setEnabled(false);
			return;
		}
		if (newValue instanceof Boolean){
			getView().setEnabled((Boolean)newValue);
		}
	}

	@Override
	public Boolean get() {
		return getView().isEnabled();
	}
}
