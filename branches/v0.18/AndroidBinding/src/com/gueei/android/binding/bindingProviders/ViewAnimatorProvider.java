package com.gueei.android.binding.bindingProviders;

import android.view.View;
import android.widget.ViewAnimator;

import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.viewAttributes.ChildViewsViewAttribute;
import com.gueei.android.binding.viewAttributes.DisplayedChildViewAttribute;

public class ViewAnimatorProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof ViewAnimator)) return null;
		if (attributeId.equals("displayedChild")){
			DisplayedChildViewAttribute attr = new 
				DisplayedChildViewAttribute((ViewAnimator)view);
			return (ViewAttribute<Tv, ?>) attr;
		}
		if (attributeId.equals("childViews")){
			ChildViewsViewAttribute attr = new 
				ChildViewsViewAttribute((ViewAnimator)view);
			return (ViewAttribute<Tv, ?>) attr;
		}
		return null;
	}

	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof ViewAnimator)) return;
		bindViewAttribute(view, map, model, "childViews");
		bindViewAttribute(view, map, model, "displayedChild");
	}
}
