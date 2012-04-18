package gueei.binding.viewAttributes.progressBar;

import gueei.binding.ViewAttribute;
import android.util.FloatMath;
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
			getView().setSecondaryProgress((int)FloatMath.ceil((Float)newValue * PROGRESS_MAX));
		}
	}

	@Override
	public Float get() {
		return (float)getView().getSecondaryProgress() / (float)PROGRESS_MAX;
	}
}
