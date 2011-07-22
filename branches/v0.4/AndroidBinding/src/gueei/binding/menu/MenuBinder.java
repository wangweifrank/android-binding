package gueei.binding.menu;

import gueei.binding.Binder;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;

// Each MenuBinder correspond to one AbsMenuBridge xml. 
// Instance should be kept by the activity
public class MenuBinder {
	private boolean firstCreate = true;
	private final int mMenuResId;
	private ArrayList<AbsMenuBridge> items = 
			new ArrayList<AbsMenuBridge>();
	
	public MenuBinder(int menuResId){
		mMenuResId = menuResId;
	}
	
	// Called by owner activity
	public boolean onCreateOptionsMenu(Activity activity, Menu menu, Object model){
		// First inflate the menu - default action
		activity.getMenuInflater().inflate(mMenuResId, menu);
		
		if (firstCreate){
			// Now, parse the menu
			XmlResourceParser parser = activity.getResources().getXml(mMenuResId);
			try{
				int eventType= parser.getEventType();
				while(eventType != XmlResourceParser.END_DOCUMENT){
					if (eventType==XmlResourceParser.START_TAG){
						int id = parser.getAttributeResourceValue(Binder.ANDROID_NAMESPACE, "id", -1);
						String nodeName = parser.getName();
						if (id>0){
							AttributeSet attrs = Xml.asAttributeSet(parser);
							AbsMenuBridge item = null;
							if ("item".equals(nodeName)){
								item = MenuItemBridge.create(id, attrs, activity, model);
							}else if ("group".equals(nodeName)){
								item = MenuGroupBridge.create(id, attrs, activity, model);
							}
							if (item!=null){
								items.add(item);
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
			firstCreate = false;
		}
		
		for(AbsMenuBridge item: items){
			item.onCreateOptionItem(menu);
		}
		
		return true;
	}
	
	public boolean onPrepareOptionsMenu(Activity activity, Menu menu){
		for(AbsMenuBridge item: items){
			item.onPrepareOptionItem(menu);
		}
		return true;
	}
}
