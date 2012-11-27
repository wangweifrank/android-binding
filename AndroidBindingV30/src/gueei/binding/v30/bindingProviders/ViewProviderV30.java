package gueei.binding.v30.bindingProviders;

import android.view.View;
import gueei.binding.ViewAttribute;
import gueei.binding.bindingProviders.BindingProvider;
import gueei.binding.v30.viewAttributes.view.BackgroundViewAttributeV30;
import gueei.binding.v30.viewAttributes.view.OnAttachViewAttributeV30;
import gueei.binding.v30.viewAttributes.view.OnDetachViewAttributeV30;

public class ViewProviderV30 extends BindingProvider{
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (attributeId.equals("onAttach"))
			return (ViewAttribute<Tv, ?>) new OnAttachViewAttributeV30(view);
		if (attributeId.equals("onDetach"))
			return (ViewAttribute<Tv, ?>) new OnDetachViewAttributeV30(view);	
		if (attributeId.equals("background"))
			return (ViewAttribute<Tv, ?>) new BackgroundViewAttributeV30(view);			
		return null;
	}
}
