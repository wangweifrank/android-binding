package gueei.binding.bindingProviders;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.horizontalScrollView.HorizontalPositionViewAttribute;
import gueei.binding.viewAttributes.tabHost.TabWidthViewAttribute;

/**
 * User: =ra=
 * Date: 17.08.11
 * Time: 23:31
 */
public class HorizontalScrollViewProvider extends BindingProvider {

	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof HorizontalScrollView)) {
			return null;
		}
		else if (attributeId.equals("horizPosition")) {
			return (ViewAttribute<Tv, ?>) new HorizontalPositionViewAttribute((HorizontalScrollView) view);
		}
		return null;
	}
}
