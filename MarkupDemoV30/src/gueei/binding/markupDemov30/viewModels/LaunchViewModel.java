package gueei.binding.markupDemov30.viewModels;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;
import gueei.binding.markupDemov30.R;
import gueei.binding.observables.IntegerObservable;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;

public class LaunchViewModel {
	private Context mContext;
	
	public final ActionBar ActionBarViewModel;
	
	public final Observable<DemoCategory> SelectedCategory =
			new Observable<DemoCategory>(DemoCategory.class);
	
	public final ArrayListObservable<DemoCategory> Categories =
			new ArrayListObservable<DemoCategory>(DemoCategory.class);

	public final Observable<Object> CurrentViewModel = 
			new Observable<Object>(Object.class);
	
	public final IntegerObservable CurrentLayout =
			new IntegerObservable(0);
	
	public final Command CategorySelected = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			SelectedCategory.get().showFirstDemo();
		}
	};
	
	public LaunchViewModel(Context context){
		mContext = context;

		parseDemos();
		ActionBarViewModel = new ActionBar();
		// Event Aggregator is a global event registered to the specified context
		// It is supposed to be an in-app mini intent broadcaster
		DemoCategory layout = new DemoCategory(mContext, "Layouts");
		Categories.add(layout);

		EventAggregator.getInstance(context)
			.subscribe("ShowDemo", new EventSubscriber(){
				@Override
				public void onEventTriggered(String eventName,
						Object publisher, Bundle data) {
					showDemo(data.getString("Clazz"), data.getInt("Layout", 0));
				}
			});
	}
	
	private void showDemo(String demoClassName, int layout){
		try {
			Object vm = Class.forName(demoClassName).newInstance();
			CurrentViewModel.set(vm);
			CurrentLayout.set(layout);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void parseDemos(){
		DemoCategory current = null;
		XmlResourceParser parser = mContext.getResources().getXml(R.xml.demos);
		try {
			int eventType = parser.getEventType();
			while(eventType != XmlResourceParser.END_DOCUMENT){
				switch(eventType){
				case XmlResourceParser.START_TAG:
						if (parser.getName().equals("category") && current == null){
							current = new DemoCategory(mContext, parser.getAttributeValue(null, "name"));
						}
						if (parser.getName().equals("entry")){
							if (current==null)
								throw new Exception();
							current.Entries.add(new DemoEntry(
									parser.getAttributeValue(null, "name"),
									resolveVM(parser.getAttributeValue(null, "vm")),
									resolveLayout(parser.getAttributeValue(null, "layout"))));
						}
					break;
				case XmlResourceParser.END_TAG:
						if (parser.getName().equals("category")){
							if (current==null)
								throw new Exception();
							Categories.add(current);
							current = null;
						}
					break;
				}
				eventType = parser.next();
			}
		}catch(Exception e){}
	}
	
	private int resolveLayout(String name) throws Exception{
		Class<?> layoutClass = R.layout.class;
		return layoutClass.getField(name).getInt(null);
	}
	
	private Class<?> resolveVM(String name) throws Exception{
		String pkgName = "gueei.binding.markupDemov30.viewModels.";
		return Class.forName(pkgName + name);
	}
}
