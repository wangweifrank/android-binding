package com.gueei.android.binding.viewAttributes;

import android.widget.ProgressBar;

import com.gueei.android.binding.ViewAttribute;

public class SecondaryProgressViewAttribute extends ViewAttribute<ProgressBar, Float> {
	public SecondaryProgressViewAttribute(ProgressBar view) {
		super(Float.class, view, "progress");
		getView().setSecondaryProgress(0);
		getView().setMax(1000);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null) return;
		if (newValue instanceof Float){
			getView().setSecondaryProgress((int)Math.ceil((Float)newValue * 1000));
		}
	}

	@Override
	public Float get() {
		return (float)getView().getSecondaryProgress() / 1000f;
	}
}
