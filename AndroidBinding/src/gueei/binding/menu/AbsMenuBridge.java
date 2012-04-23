package gueei.binding.menu;

import java.util.Collection;

import gueei.binding.Binder;
import gueei.binding.BindingSyntaxResolver;
import gueei.binding.ConstantObservable;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.labs.EventAggregator;
import android.app.Activity;
import android.os.Bundle;
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
			item = new MenuItemBridge(id, attrs, activity, model, true);
		}else if ("group".equals(nodeName)){
			item = new MenuGroupBridge(id, attrs, activity, model, true);
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
	
	protected IObservable<?> getObservableFromStatement(Activity activity, AttributeSet attributes, String attrName, Object model, boolean subscribe){
		String attrValue = attributes.getAttributeValue(Binder.BINDING_NAMESPACE, attrName);
		if (attrValue!=null){
			IObservable<?> obs = BindingSyntaxResolver.constructObservableFromStatement(activity, attrValue, model);
			if (subscribe && !(obs instanceof ConstantObservable)){
				if (observer==null)
					observer = new OptionsItemObserver(activity);
				obs.subscribe(observer);
			}
				
			return obs;
		}
		return null;
	}

	public abstract boolean onOptionsItemSelected(MenuItem item);
	
	private OptionsItemObserver observer;
	private class OptionsItemObserver implements Observer{
		private Activity mActivity;
		
		public OptionsItemObserver(Activity activity){
			mActivity = activity;
		}
		
		@Override
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			EventAggregator.getInstance(mActivity).publish("invalidateOptionsMenu()", prop, new Bundle());
		}
	};
}
