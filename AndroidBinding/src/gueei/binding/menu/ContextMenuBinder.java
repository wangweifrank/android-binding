package gueei.binding.menu;

import gueei.binding.Binder;

import java.io.IOException;
import java.util.Hashtable;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

public class ContextMenuBinder implements View.OnCreateContextMenuListener, OnMenuItemClickListener{
	private final int mMenuResId;
	private final Object mViewModel;
	private Hashtable<Integer, AbsMenuBridge> items = 
			new Hashtable<Integer, AbsMenuBridge>();
	
	public ContextMenuBinder(int menuResId, Object viewModel){
		mMenuResId = menuResId;
		mViewModel = viewModel;
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		// Activity should be the context, or else, nothing can do
		if (!(v.getContext() instanceof Activity)) return;
		// First inflate the menu - default action
		Activity activity = (Activity)v.getContext();
		activity.getMenuInflater().inflate(mMenuResId, menu);
		
		// Now, parse the menu
		XmlResourceParser parser = activity.getResources().getXml(mMenuResId);
		try{
			int eventType= parser.getEventType();
			while(eventType != XmlResourceParser.END_DOCUMENT){
				if (eventType==XmlResourceParser.START_TAG){
					int id = parser.getAttributeResourceValue(Binder.ANDROID_NAMESPACE, "id", -1);
					MenuItem mi = menu.findItem(id);
					if (mi!=null){
						mi.setOnMenuItemClickListener(this);
						
						String nodeName = parser.getName();
						if (id>0){
							AttributeSet attrs = Xml.asAttributeSet(parser);
							AbsMenuBridge item = null;
							if ("item".equals(nodeName)){
								item = MenuItemBridge.create(id, attrs, activity, mViewModel);
							}else if ("group".equals(nodeName)){
								item = MenuGroupBridge.create(id, attrs, activity, mViewModel);
							}
							if (item!=null){
								items.put(id, item);
							}
						}
					}
				}
				eventType = parser.next();
			}
		}catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException ex){
			ex.printStackTrace();
		}

		for(AbsMenuBridge item: items.values()){
			item.onCreateOptionItem(menu);
			item.onPrepareOptionItem(menu);
		}
	}
	
	public boolean onMenuItemClick(MenuItem mi) {
		AbsMenuBridge item = items.get(mi.getItemId());
		if (item!=null){
			return item.onOptionsItemSelected(mi);
		}
		return false;
	}
}
