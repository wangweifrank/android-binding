package gueei.binding.v30.bindingProviders;

import android.view.View;
import gueei.binding.ViewAttribute;
import gueei.binding.bindingProviders.BindingProvider;
import gueei.binding.v30.viewAttributes.AnimationViewAttributeV30;

public class ViewProviderV30 extends BindingProvider{
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (attributeId.equals("animation"))
			return (ViewAttribute<Tv, ?>) new AnimationViewAttributeV30(view);
		return null;
	}
}
