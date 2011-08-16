package gueei.binding.viewAttributes.tabHost;

import android.view.View;
import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;

/**
 * User: =ra=
 * Date: 16.08.11
 * Time: 19:26
 */
public class TabWidthViewAttribute extends ViewAttribute<View, Integer> {

	private int mTabWidth = -1;

	public TabWidthViewAttribute(View view) {
		super(Integer.class, view, "tabWidth");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null) {
			mTabWidth = -1;
			return;
		}
		if (newValue instanceof Integer) {
			mTabWidth = (Integer) newValue;
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}

	@Override
	public Integer get() {
		return mTabWidth;
	}
}
