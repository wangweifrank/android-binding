package gueei.binding.menu;

import android.app.Activity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import gueei.binding.Command;
import gueei.binding.IObservable;

/**
 * Mock menu item act as bridging bindable attributes
 * with the real menu item
 * Will not keep reference to real menu item since
 * menu item might suddenly detached
 * @author andy
 *
 */
public class MenuItemBridge extends AbsMenuBridge{

	private Command onClickCommand;
	
	private IObservable<?> title, visible, enabled, checked;
	
	public MenuItemBridge(int id){
		super(id);
	}
	
	public void onCreateOptionItem(Menu menu){
		MenuItem item = menu.findItem(mId);
	}
	
	public void onPrepareOptionItem(Menu menu){
		MenuItem item = menu.findItem(mId);
		if (item==null) return;
		if (title!=null){
			Object titleObj = title.get();
			if(titleObj==null) return;
			if (titleObj instanceof CharSequence) item.setTitle((CharSequence)titleObj);
			else
				item.setTitle(titleObj.toString());
		}
		if (visible!=null){
			item.setVisible(Boolean.TRUE.equals(visible.get()));
		}
		if (enabled!=null){
			item.setEnabled(Boolean.TRUE.equals(enabled.get()));
		}
		if (checked!=null){
			Log.d("Binder", "prepare: " + Boolean.TRUE.equals(checked.get()));
			item.setChecked(Boolean.TRUE.equals(checked.get()));
		}
	}
	
	public static AbsMenuBridge create(int id, AttributeSet attributes,
			Activity activity, Object model) {
		MenuItemBridge bridge = new MenuItemBridge(id);
		IObservable<?> temp = getObservableFromAttribute(activity, attributes, "onClick", model);
		if ((temp!=null)&&(temp instanceof Command)){
			bridge.onClickCommand = (Command)temp;
		}
		temp = getObservableFromAttribute(activity, attributes, "title", model);
		if ((temp!=null)){
			bridge.title = temp;
		}
		temp = getObservableFromAttribute(activity, attributes, "visible", model);
		if ((temp!=null)){
			bridge.visible = temp;
		}
		temp = getObservableFromAttribute(activity, attributes, "enabled", model);
		if ((temp!=null)){
			bridge.enabled = temp;
		}
		temp = getObservableFromAttribute(activity, attributes, "checked", model);
		if ((temp!=null)){
			bridge.checked = temp;
		}		
		return bridge;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean output = false;
		if (onClickCommand!=null){
			onClickCommand.Invoke(null, item);
			output = true;
		}

		if (checked!=null){
			if (Boolean.class.isAssignableFrom(checked.getType()))
				((IObservable<Boolean>)checked).set(!item.isChecked());
			Log.d("Binder", "Checked: "+ item.isChecked() + " " + checked.get());
		}
		return output;
	}
}
