package gueei.binding.v30.widget;

import gueei.binding.Binder;
import gueei.binding.menu.AbsMenuBridge;
import gueei.binding.menu.MenuGroupBridge;
import gueei.binding.menu.MenuItemBridge;

import java.io.IOException;
import java.util.Hashtable;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.app.Activity;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.PopupMenu;

public class PopupMenuBinderV30 implements OnMenuItemClickListener {
	private Hashtable<Integer, AbsMenuBridge> items = 
			new Hashtable<Integer, AbsMenuBridge>();	
	
	public void createPopupMenu(View v, PopupMenu popup, int menuResId, Object viewModel) {

		// First inflate the menu - default action
		Activity activity = (Activity)v.getContext();
		popup.getMenuInflater().inflate(menuResId, popup.getMenu());
		
		// Now, parse the menu
		XmlResourceParser parser = activity.getResources().getXml(menuResId);
		try{
			int eventType= parser.getEventType();
			while(eventType != XmlResourceParser.END_DOCUMENT){
				if (eventType==XmlResourceParser.START_TAG){
					int id = parser.getAttributeResourceValue(Binder.ANDROID_NAMESPACE, "id", -1);
					MenuItem mi = popup.getMenu().findItem(id);
					if (mi!=null){
						mi.setOnMenuItemClickListener(this);
						
						String nodeName = parser.getName();
						if (id>0){
							AttributeSet attrs = Xml.asAttributeSet(parser);
							AbsMenuBridge item = null;
							if ("item".equals(nodeName)){
								item = MenuItemBridge.create(id, attrs, activity, viewModel);
							}else if ("group".equals(nodeName)){
								item = MenuGroupBridge.create(id, attrs, activity, viewModel);
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
			item.onCreateOptionItem(popup.getMenu());
			item.onPrepareOptionItem(popup.getMenu());
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
