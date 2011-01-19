package com.gueei.android.binding.viewAttributes;


import android.view.View;
import com.gueei.android.binding.ViewAttribute;

public class VisibilityViewAttribute extends ViewAttribute<View, Integer> {

	public VisibilityViewAttribute(View view, String attributeName) {
		super(view, attributeName);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue==null){
			this.view.get().setVisibility(View.GONE);
			return;
		}
		if (newValue instanceof Boolean){
			if ((Boolean)newValue)
				this.view.get().setVisibility(View.VISIBLE);
			else
				this.view.get().setVisibility(View.GONE);
			return;
		}
		if (newValue instanceof Integer){
			this.view.get().setVisibility((Integer)newValue);
		}
		this.view.get().setVisibility(View.VISIBLE);
	}

	@Override
	public Integer get() {
		return this.view.get().getVisibility();
	}
}
