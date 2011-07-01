package gueei.binding.bindingProviders;

import gueei.binding.BindingMap;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnSeekBarChangeListenerMulticast;
import gueei.binding.viewAttributes.seekBar.OnSeekBarChangeViewEvent;
import android.view.View;
import android.widget.SeekBar;


public class SeekBarProvider extends BindingProvider {
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof SeekBar)) return null;
		if (attributeId.equals("onSeekBarChange"))
			return (ViewAttribute<Tv, ?>) new OnSeekBarChangeViewEvent((SeekBar)view);
		return null;
	}

	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof SeekBar)) return;
		bindViewAttribute(view, map, model, "onSeekBarChange");
	}
}