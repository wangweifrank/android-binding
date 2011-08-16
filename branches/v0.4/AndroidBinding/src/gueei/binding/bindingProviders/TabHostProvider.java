package gueei.binding.bindingProviders;

import android.view.View;
import android.widget.TabHost;
import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.tabHost.TabSelectedPositionViewAttribute;
import gueei.binding.viewAttributes.tabHost.TabTemplateViewAttribute;
import gueei.binding.viewAttributes.tabHost.TabWidthViewAttribute;
import gueei.binding.viewAttributes.tabHost.TabsViewAttribute;

public class TabHostProvider extends BindingProvider {

	// TODO: SelectPosition fix if Tab selected from UI
	@SuppressWarnings("unchecked") @Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof TabHost)) {
			return null;
		}
		else if (attributeId.equals("tabWidth")) {
			return (ViewAttribute<Tv, ?>) new TabWidthViewAttribute((TabHost) view);
		}
		else if (attributeId.equals("selectedPosition")) {
			return (ViewAttribute<Tv, ?>) new TabSelectedPositionViewAttribute((TabHost) view);
		}
		else if (attributeId.equals("tabTemplate")) {
			return (ViewAttribute<Tv, ?>) new TabTemplateViewAttribute((TabHost) view);
		}
		else if (attributeId.equals("tabs")) {
			return (ViewAttribute<Tv, ?>) new TabsViewAttribute((TabHost) view);
		}
		return null;
	}
}