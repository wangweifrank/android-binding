package gueei.binding.menu;

import gueei.binding.IObservable;
import android.app.Activity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

public class MenuGroupBridge extends AbsMenuBridge {

	protected MenuGroupBridge(int id) {
		super(id);
	}

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
	public static AbsMenuBridge create(int id, AttributeSet attributes,
			Activity activity, Object model) {
		MenuGroupBridge bridge = new MenuGroupBridge(id);
		
		// Assume id is created
		IObservable<?> temp = getObservableFromAttribute(activity, attributes, "visible", model);
		if ((temp!=null)&&(Boolean.class.isAssignableFrom(temp.getType()))){
			bridge.mVisible = (IObservable<Boolean>)temp;
		}
		
		return bridge;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
}
