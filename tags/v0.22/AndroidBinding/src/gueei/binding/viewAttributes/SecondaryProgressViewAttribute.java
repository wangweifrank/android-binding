package gueei.binding.viewAttributes;

import gueei.binding.ViewAttribute;
import android.widget.ProgressBar;


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
