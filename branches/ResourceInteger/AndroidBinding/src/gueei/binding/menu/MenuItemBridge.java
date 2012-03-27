package gueei.binding.menu;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
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
	
	private IObservable<?> title, visible, enabled, checked, icon;
	
	public MenuItemBridge(int id){
		super(id);
	}
	
	public void onCreateOptionItem(Menu menu){
		
	}
	
	public void onPrepareOptionItem(Menu menu){
		MenuItem item = menu.findItem(mId);
		if (item==null) return;
		if (title!=null){
			Object titleObj = title.get();
			if(titleObj!=null){
				if (titleObj instanceof CharSequence) item.setTitle((CharSequence)titleObj);
				else
					item.setTitle(titleObj.toString());
			}else{
				item.setTitle("");
			}
		}
		if (visible!=null){
			item.setVisible(Boolean.TRUE.equals(visible.get()));
		}
		if (enabled!=null){
			item.setEnabled(Boolean.TRUE.equals(enabled.get()));
		}
		if (checked!=null){
			item.setChecked(Boolean.TRUE.equals(checked.get()));
		}
		if (icon!=null){
			Object iconObj = icon.get();
			if (iconObj!=null){
				if (iconObj instanceof Integer)
					item.setIcon((Integer)iconObj);
				else if (iconObj instanceof Drawable)
					item.setIcon((Drawable)iconObj);
			}else{
				item.setIcon(null);
			}
		}
	}
	
	public static AbsMenuBridge create(int id, AttributeSet attributes,
			Activity activity, Object model) {
		MenuItemBridge bridge = new MenuItemBridge(id);
		IObservable<?> temp = getObservableFromAttribute(activity, attributes, "onClick", model);
		if ((temp!=null)&&(temp.get() instanceof Command)){
			bridge.onClickCommand = (Command)temp.get();
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
		temp = getObservableFromAttribute(activity, attributes, "icon", model);
		if ((temp!=null)){
			bridge.icon = temp;
		}
		return bridge;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean output = false;
		if (onClickCommand!=null){
			if(item.getMenuInfo() instanceof AdapterView.AdapterContextMenuInfo) {
			    AdapterView.AdapterContextMenuInfo cmi = 
				        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo (); 
			    int position = cmi.position;			
			    onClickCommand.InvokeCommand(null, item, position);
			} else {
				onClickCommand.InvokeCommand(null, item);
			}
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
