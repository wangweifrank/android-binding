package gueei.binding.viewAttributes.progressBar;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnSeekBarChangeListenerMulticast;
import android.util.FloatMath;
import android.widget.ProgressBar;
import android.widget.SeekBar;

/*
 * Accepts Float from 0 - 1, and translate the result to a 1000-based fraction
 * as the progress.
 * i.e. progress of progress bar will become 
 */
public class ProgressViewAttribute extends ViewAttribute<ProgressBar, Float>
	implements SeekBar.OnSeekBarChangeListener{
	
	private static final int PROGRESS_MAX = 10000;
	
	public ProgressViewAttribute(ProgressBar view) {
		super(Float.class, view, "progress");
		getView().setProgress(0);
		getView().setMax(PROGRESS_MAX);
		if (view instanceof SeekBar){
			Binder.getMulticastListenerForView(view, OnSeekBarChangeListenerMulticast.class)
				.register(this);
		}
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null) return;
		if (newValue instanceof Float){
			getView().setProgress((int)FloatMath.ceil((Float)newValue * PROGRESS_MAX));
		}
	}

	@Override
	public Float get() {
		return (float)getView().getProgress() / (float)PROGRESS_MAX;
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser){
			this.notifyChanged();
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}
