package gueei.binding.bindingProviders;

import gueei.binding.BindingMap;
import gueei.binding.ViewAttribute;
import android.view.View;
import android.widget.SeekBar;


public class SeekBarProvider extends BindingProvider {
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		return null;
	}

	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof SeekBar)) return;
	}
}
