package com.gueei.android.binding.bindingProviders;

import android.view.View;
import android.widget.AbsSpinner;

import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.ViewAttribute;

public class AbsSpinnerViewProvider extends BindingProvider {

	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof AbsSpinner)) return null;
		return null;
	}

	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof AbsSpinner)) return;
	}
}
