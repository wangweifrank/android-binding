package gueei.binding.menu;

import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import gueei.binding.Command;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.labs.EventAggregator;

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
		
	public void onCreateOptionItem(Menu menu){
		
	}
	
	public void onPrepareOptionItem(Menu menu){
		MenuItem item = menu.findItem(mId);
		if (item==null) return;
		if (title!=null){
			Object titleObj = title.get();
			if(titleObj!=null){
				// Title don't allow HTML Formatting
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
	
	public MenuItemBridge(int id, AttributeSet attributes,
			Activity activity, Object model, boolean subscribe) {
		super(id);
		IObservable<?> temp = getObservableFromStatement(activity, attributes, "onClick", model, subscribe);
		if ((temp!=null)&&(temp.get() instanceof Command)){
			onClickCommand = (Command)temp.get();
		}
		temp = getObservableFromStatement(activity, attributes, "title", model, subscribe);
		if ((temp!=null)){
			title = temp;
		}
		temp = getObservableFromStatement(activity, attributes, "visible", model, subscribe);
		if ((temp!=null)){
			visible = temp;
		}
		temp = getObservableFromStatement(activity, attributes, "enabled", model, subscribe);
		if ((temp!=null)){
			enabled = temp;
		}
		temp = getObservableFromStatement(activity, attributes, "checked", model, subscribe);
		if ((temp!=null)){
			checked = temp;
		}
		temp = getObservableFromStatement(activity, attributes, "icon", model, subscribe);
		if ((temp!=null)){
			icon = temp;
		}
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
