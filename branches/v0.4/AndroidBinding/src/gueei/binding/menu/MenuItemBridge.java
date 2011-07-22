package gueei.binding.menu;

import android.app.Activity;
import android.util.AttributeSet;
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
public class MenuItemBridge extends AbsMenuBridge
	implements android.view.MenuItem.OnMenuItemClickListener{

	private Command onClickCommand;
	
	private IObservable<?> title, visible, enabled;
	
	public MenuItemBridge(int id){
		super(id);
	}

	public boolean onMenuItemClick(android.view.MenuItem item) {
		if (onClickCommand!=null){
			onClickCommand.Invoke(null, item);
			return true;
		}
		return false;
	}
	
	public void onCreateOptionItem(Menu menu){
		MenuItem item = menu.findItem(mId);
		if (item!=null){
			item.setOnMenuItemClickListener(this);
		}
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
		return bridge;
	}
}
