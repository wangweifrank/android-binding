package gueei.binding.viewAttributes.tabHost;

import android.view.View;
import android.widget.TabHost;
import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;

/**
 * User: =ra=
 * Date: 16.08.11
 * Time: 20:46
 */
public class TabSelectedPositionViewAttribute extends ViewAttribute<View, Integer> {

	private int mTabSelectedPosition = -1;
	public TabSelectedPositionViewAttribute(View view) {
		super(Integer.class, view, "selectedPosition");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null) {
			return;
		}
		if (newValue instanceof Integer) {
			mTabSelectedPosition= (Integer)newValue;
			((TabHost)getView()).setCurrentTab(mTabSelectedPosition);
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.TwoWay;
	}

	@Override
	public Integer get() {
		return mTabSelectedPosition;
	}
}
