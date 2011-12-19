package gueei.binding.menu;

import gueei.binding.Binder;
import gueei.binding.BindingSyntaxResolver;
import gueei.binding.IObservable;
import android.app.Activity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

/**
 * An instance of AbsMenuBridge maps to one xml
 * @author andy
 *
 */
public abstract class AbsMenuBridge {
	
	// Factory method
	public static AbsMenuBridge create
		(String nodeName, int id, AttributeSet attrs, Activity activity, Object model){
		AbsMenuBridge item;
		
		if ("item".equals(nodeName)){
			item = MenuItemBridge.create(id, attrs, activity, model);
		}else if ("group".equals(nodeName)){
			item = MenuGroupBridge.create(id, attrs, activity, model);
		}else{
			item = null;
		}
		
		return item;
	}
	
	protected final int mId;
	protected AbsMenuBridge(int id){
		mId = id;
	}
	
	public abstract void onCreateOptionItem(Menu menu);
	
	public abstract void onPrepareOptionItem(Menu menu);
	
	protected static IObservable<?> getObservableFromAttribute(Activity activity, AttributeSet attributes, String attrName, Object model){
		String attrValue = attributes.getAttributeValue(Binder.BINDING_NAMESPACE, attrName);
		if (attrValue!=null){
			return BindingSyntaxResolver.constructObservableFromStatement(activity, attrValue, model);
		}
		return null;
	}

	public abstract boolean onOptionsItemSelected(MenuItem item);
}
