package gueei.binding.v30;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.BindingLog;
import gueei.binding.BindingMap;
import gueei.binding.ViewAttribute;
import gueei.binding.v30.actionbar.BindableActionBar;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;

public class ActivityBinder {
	public static BindableActionBar inflateActionBar(Activity activity, int xmlId){
		BindableActionBar actionBar = null;
		XmlResourceParser parser = activity.getResources().getXml(xmlId);
		try {
			int eventType = parser.getEventType();
			while(eventType != XmlResourceParser.END_DOCUMENT){
				switch(eventType){
				case XmlResourceParser.START_TAG:
					if ("actionbar".equals(parser.getName())){
						actionBar = new BindableActionBar(activity);
						AttributeSet attrs = Xml.asAttributeSet(parser);
						
						BindingMap map = new BindingMap();
						int count = attrs.getAttributeCount();
						for(int i=0; i<count; i++){
							String attrName = attrs.getAttributeName(i);
							String attrValue = attrs.getAttributeValue(Binder.BINDING_NAMESPACE, attrName);
							if (attrValue!=null){
								map.put(attrName, attrValue);
							}
						}
						
						Binder.putBindingMapToView(actionBar, map);
					}
					break;
				}
				eventType = parser.next();
			}
			return actionBar;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void BindActionBar(Activity activity, BindableActionBar v, Object model){
		ActionBarAttributeBinder binder = new ActionBarAttributeBinder();
		binder.bindView(activity, v, model);
	}
	
	private static class ActionBarAttributeBinder extends AttributeBinder{
		@Override
		public ViewAttribute<?, ?> createAttributeForView(View view,
				String attributeId) {
			if (!(view instanceof BindableActionBar)) return null;
			try{
				String capId = attributeId.substring(0, 1).toUpperCase() + attributeId.substring(1);
				String className = "gueei.binding.v30.actionbar.attributes." + capId;
				return (ViewAttribute<?,?>)Class.forName(className)
							.getConstructor(BindableActionBar.class)
							.newInstance((BindableActionBar)view);
			}catch(Exception e){
				BindingLog.warning("ActionBarAttributeBinder", "Attribute not found");
				return null;
			}
		}
	}
}
