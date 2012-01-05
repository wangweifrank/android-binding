package gueei.binding.v30.bindingProviders;

import android.support.v4.view.ViewPager;
import android.view.View;
import gueei.binding.ViewAttribute;
import gueei.binding.bindingProviders.BindingProvider;
import gueei.binding.v30.viewAttributes.adapterView.viewPager.ItemLayoutTemplateViewAttribute;
import gueei.binding.v30.viewAttributes.adapterView.viewPager.ViewPager_ItemSourceViewAttribute;

public class ViewPagerViewProviderV30 extends BindingProvider {
	@SuppressWarnings({ "unchecked"})
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (!(view instanceof ViewPager))
			return null;
		try {
			if (attributeId.equals("itemSource")){
				if (view instanceof android.support.v4.view.ViewPager){
					return (ViewAttribute<Tv, ?>)
						new ViewPager_ItemSourceViewAttribute((android.support.v4.view.ViewPager)view);
				}
				return null;
			} else if (attributeId.equals("itemLayout")){
				return (ViewAttribute<Tv, ?>)new ItemLayoutTemplateViewAttribute(view, "itemLayout");
			}
		} catch (Exception e) {
			// Actually it should never reach this statement
		}
		return null;
	}
}
