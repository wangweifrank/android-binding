package com.gueei.android.binding.viewAttributes;

import android.view.View;
import com.gueei.android.binding.ViewAttribute;

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
