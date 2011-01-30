package com.gueei.android.binding.bindingProviders;

import android.view.View;
import android.widget.ProgressBar;

import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.viewAttributes.ProgressViewAttribute;
import com.gueei.android.binding.viewAttributes.SecondaryProgressViewAttribute;

public class ProgressBarProvider extends BindingProvider {
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof ProgressBar)) return null;
		if (attributeId.equals("progress")){
			return (ViewAttribute<Tv, ?>)
				new ProgressViewAttribute((ProgressBar)view);
		}
		if (attributeId.equals("secondaryProgress")){
			return (ViewAttribute<Tv, ?>)
				new SecondaryProgressViewAttribute((ProgressBar)view);
		}
		return null;
	}


	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof ProgressBar)) return;
		bindViewAttribute(view, map, model, "progress");
		bindViewAttribute(view, map, model, "secondaryProgress");
	}
}
