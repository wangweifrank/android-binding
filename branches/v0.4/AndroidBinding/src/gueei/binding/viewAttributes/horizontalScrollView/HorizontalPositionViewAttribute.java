package gueei.binding.viewAttributes.horizontalScrollView;

import android.widget.HorizontalScrollView;
import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.view.View;

/**
 * User: =ra=
 * Date: 17.08.11
 * Time: 23:34
 */
public class HorizontalPositionViewAttribute extends ViewAttribute<HorizontalScrollView, Integer> {

	public HorizontalPositionViewAttribute(HorizontalScrollView view) {
		super(Integer.class, view, "horizPosition");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null) {
			return;
		}
		if (newValue instanceof Integer) {
			HorizontalScrollView horizontalScrollView = (HorizontalScrollView) getView();
			horizontalScrollView.scrollTo((Integer) newValue, horizontalScrollView.getScrollY());
			return;
		}
	}

	@Override
	public Integer get() {
		//TODO: Not working properly, need to be fixed
		HorizontalScrollView horizontalScrollView = (HorizontalScrollView) getView();
		return horizontalScrollView.getScrollX();
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (Integer.class.isAssignableFrom(type)) {
			return BindingType.TwoWay;
		}
		return BindingType.NoBinding;
	}
}
