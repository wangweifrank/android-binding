package gueei.binding.v30.actionbar.attributes;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.v30.actionbar.BindableActionBar;
import android.app.ActionBar;
import android.widget.Adapter;
import android.widget.SpinnerAdapter;

public class TabNavigationAdapter extends ViewAttribute<BindableActionBar, Adapter> {

	public TabNavigationAdapter(BindableActionBar view) {
		super(Adapter.class, view, "ListNavigationAdapter");
	}

	private Adapter mAdapter;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue instanceof Adapter){
			mAdapter = (Adapter)newValue;
			setupTab();
		}
	}

	/** Setup the tab for the action bar
	 * 
	 */
	private void setupTab() {
		ActionBar ab = getView().getActionBar();
	}

	@Override
	public Adapter get() {
		return mAdapter;
	}
	
	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (SpinnerAdapter.class.isAssignableFrom(type)){
			return BindingType.TwoWay;
		}
		if (Adapter.class.isAssignableFrom(type)){
			return BindingType.OneWay;
		}
		return BindingType.NoBinding;
	}
}
