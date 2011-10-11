package gueei.binding.v30.actionbar;

import gueei.binding.BindingSyntaxResolver;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.XmlResourceParser;

public class ActionBarBinder {
	public static void BindActionBar(Activity activity, int xmlId, Object model){
		XmlResourceParser parser = activity.getResources().getXml(xmlId);
		try {
			int eventType = parser.getEventType();
			while(eventType != XmlResourceParser.END_DOCUMENT){
				switch(eventType){
				case XmlResourceParser.START_TAG:
					// Only work for actionbar
					if ("actionbar".equals(parser.getName())){
						ActionBar bar = activity.getActionBar();
						if (bar!=null)
							bindActionBar(activity, bar, parser, model);
						bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
				        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				        bar.setDisplayShowHomeEnabled(true);
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}

	private static void bindActionBar(Activity activity, ActionBar bar, XmlResourceParser parser, Object model) 
		throws IOException, XmlPullParserException{
		// Process attributes of ActionBar
		int customView = parser.getAttributeResourceValue(null, "view", -1);
		if (customView >0)
			bar.setCustomView(customView);
		
		// Should parse until end of actionbar tag
		int eventType = 0;
		while ((eventType = parser.next()) != XmlResourceParser.END_DOCUMENT){
			switch(eventType){
			case XmlResourceParser.START_TAG:
				if ("tab".equals(parser.getName())){
					bindActionBarTab(activity, bar, parser, model);
				}
				break;
			case XmlResourceParser.END_TAG:
				if ("actionbar".equals(parser.getName()))
					return;
			}
		}
	}

	private static void bindActionBarTab(Activity activity, ActionBar bar, XmlResourceParser parser, Object model) {
		ActionBar.Tab tab = bar.newTab();
		new OnSelectedAttribute(tab)
			.BindTo(activity, 
				BindingSyntaxResolver.constructObservableFromStatement(
						activity, parser.getAttributeValue(null, "onSelected"), model));
		
		new TextTabAttribute(tab)
			.BindTo(
				activity, 
				BindingSyntaxResolver.constructObservableFromStatement
					(activity, parser.getAttributeValue(null, "text"), model));
		bar.addTab(tab);
	}
}
