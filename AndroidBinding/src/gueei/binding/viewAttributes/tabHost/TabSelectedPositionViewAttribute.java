package gueei.binding.viewAttributes.tabHost;

import android.view.View;
import android.widget.TabHost;
import gueei.binding.Binder;
import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnTabChangedListener;

/**
 * User: =ra=
 * Date: 16.08.11
 * Time: 20:46
 */
public class TabSelectedPositionViewAttribute extends ViewAttribute<View, Integer> implements TabHost.OnTabChangeListener {

	public TabSelectedPositionViewAttribute(View view) {
		super(Integer.class, view, "selectedPosition");
		Binder.getMulticastListenerForView(view, OnTabChangedListener.class).register(this);
	}

	public void onTabChanged(String tabId) {
		int mChangedValue = ((TabHost) getView()).getCurrentTab();
		if (mChangedValue == mValue && mValueSet) {
			mValueSet = false;
		}
		else if (!mValueSet) {
			mValue = mChangedValue;
			notifyChanged(this);
		}
	}

	@Override protected void doSetAttributeValue(Object newValue) {
		if (!(newValue instanceof Integer) || (Integer) newValue == mValue) {
			return;
		}
		mValue = (Integer) newValue;
		mValueSet = true;
		((TabHost) getView()).setCurrentTab(mValue);
	}

	@Override public Integer get() {
		return mValue;
	}

	@Override protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.TwoWay;
	}

	private boolean mValueSet;
	private int     mValue;
}
