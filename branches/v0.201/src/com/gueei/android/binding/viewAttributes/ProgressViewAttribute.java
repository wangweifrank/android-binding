package com.gueei.android.binding.viewAttributes;

import android.widget.ProgressBar;

import com.gueei.android.binding.ViewAttribute;

/*
 * Accepts Float from 0 - 1, and translate the result to a 1000-based fraction
 * as the progress.
 * i.e. progress of progress bar will become 
 */
public class ProgressViewAttribute extends ViewAttribute<ProgressBar, Float> {
	
	public ProgressViewAttribute(ProgressBar view) {
		super(Float.class, view, "progress");
		getView().setProgress(0);
		getView().setMax(1000);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null) return;
		if (newValue instanceof Float){
			getView().setProgress((int)Math.ceil((Float)newValue * 1000));
		}
	}

	@Override
	public Float get() {
		return (float)getView().getProgress() / 1000f;
	}

}
