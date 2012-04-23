package gueei.binding.markupDemoICS.viewModels;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;
import gueei.binding.markupDemoICS.R;
import gueei.binding.observables.IntegerObservable;
import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;

public class LaunchViewModel {
	private Activity mContext;
	
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
	
	public LaunchViewModel(Activity context){
		mContext = context;

		parseDemos();
		ActionBarViewModel = new ActionBar();

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
			try {
				Object vm = Class.forName(demoClassName).getConstructor(Activity.class).newInstance(mContext);
				CurrentViewModel.set(vm);
				CurrentLayout.set(layout);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private int resolveLayout(String name) throws Exception{
		if (name.startsWith(".")){
			Class<?> layoutClass = com.gueei.demos.markupDemo.R.layout.class;
			return layoutClass.getField(name.substring(1)).getInt(null);
		}
		else{
			Class<?> layoutClass = R.layout.class;
			return layoutClass.getField(name).getInt(null);
		}
	}
	
	private String resolveVM(String name) throws Exception{
		String pkgName = "gueei.binding.markupDemov30.viewModels.";
		if (name.startsWith("."))
			pkgName = "com.gueei.demos.markupDemo.viewModels";
		return pkgName + name;
	}
}
