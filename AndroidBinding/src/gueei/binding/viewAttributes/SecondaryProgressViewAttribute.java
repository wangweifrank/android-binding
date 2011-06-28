package gueei.binding.viewAttributes;

import gueei.binding.ViewAttribute;
import android.widget.ProgressBar;


public class SecondaryProgressViewAttribute extends ViewAttribute<ProgressBar, Float> {
	private static final int PROGRESS_MAX = 10000;
	
	public SecondaryProgressViewAttribute(ProgressBar view) {
		super(Float.class, view, "progress");
		getView().setSecondaryProgress(0);
		getView().setMax(PROGRESS_MAX);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null) return;
		if (newValue instanceof Float){
			getView().setSecondaryProgress((int)Math.ceil((Float)newValue * PROGRESS_MAX));
		}
	}

	@Override
	public Float get() {
		return (float)getView().getSecondaryProgress() / (float)PROGRESS_MAX;
	}
}
