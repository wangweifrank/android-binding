package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.adapterView.listView.CheckedItemPositionsViewAttribute;
import gueei.binding.viewAttributes.adapterView.listView.CheckedItemPositionViewAttribute;
import android.view.View;
import android.widget.ListView;


public class ListViewProvider extends BindingProvider {

	@SuppressWarnings({ "unchecked" })
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (!(view instanceof ListView))
			return null;
		try {
			if (attributeId.equals("checkedItemPosition")) {
				return (ViewAttribute<Tv, ?>) new CheckedItemPositionViewAttribute((ListView)view);
			}else if (attributeId.equals("checkedItemPositions")) {
				return (ViewAttribute<Tv, ?>) new CheckedItemPositionsViewAttribute((ListView)view);
			}
		} catch (Exception e) {
			// Actually it should never reach this statement
		}
		return null;
	}
}
