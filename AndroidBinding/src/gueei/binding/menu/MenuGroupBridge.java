package gueei.binding.menu;

import gueei.binding.IObservable;
import android.app.Activity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

public class MenuGroupBridge extends AbsMenuBridge {

	@Override
	public void onCreateOptionItem(Menu menu) {
	}

	@Override
	public void onPrepareOptionItem(Menu menu) {
		if (mVisible!=null)
			menu.setGroupVisible(mId, mVisible.get());
	}

	private IObservable<Boolean> mVisible;
	
	@SuppressWarnings("unchecked")
	public MenuGroupBridge(int id, AttributeSet attributes,
			Activity activity, Object model, boolean subscribe) {
		super(id);

		// Assume id is created
		IObservable<?> temp = getObservableFromStatement(activity, attributes, "visible", model, subscribe);
		if ((temp!=null)&&(Boolean.class.isAssignableFrom(temp.getType()))){
			mVisible = (IObservable<Boolean>)temp;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
}
