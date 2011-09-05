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
