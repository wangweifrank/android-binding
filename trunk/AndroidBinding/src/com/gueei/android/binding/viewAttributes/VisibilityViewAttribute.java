package com.gueei.android.binding.viewAttributes;


import android.view.View;

import com.gueei.android.binding.BindingType;
import com.gueei.android.binding.ViewAttribute;

public class VisibilityViewAttribute extends ViewAttribute<View, Integer> {

	public VisibilityViewAttribute(View view, String attributeName) {
		super(Integer.class, view, attributeName);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue==null){
			getView().setVisibility(View.GONE);
			return;
		}
		if (newValue instanceof Boolean){
			if ((Boolean)newValue)
				getView().setVisibility(View.VISIBLE);
			else
				getView().setVisibility(View.GONE);
			return;
		}
		if (newValue instanceof Integer){
			getView().setVisibility((Integer)newValue);
			return;
		}
	}

	@Override
	public Integer get() {
		return getView().getVisibility();
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (Boolean.class.isAssignableFrom(type))
			return BindingType.OneWay;
		if (Integer.class.isAssignableFrom(type))
			return BindingType.TwoWay;
		return BindingType.NoBinding;
	}
}
