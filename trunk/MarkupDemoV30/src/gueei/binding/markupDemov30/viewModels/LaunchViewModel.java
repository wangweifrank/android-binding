package gueei.binding.markupDemov30.viewModels;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;
import gueei.binding.markupDemov30.R;
import gueei.binding.observables.IntegerObservable;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class LaunchViewModel {
	private Context mContext;
	
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
		}
	};
	
	public LaunchViewModel(Context context){
		mContext = context;
		
		DemoCategory source = new DemoCategory(mContext, "Source Code");
		source.Entries.add(new DemoEntry("Markup of Launch", LaunchMarkupViewModel.class, R.layout.code_view));
		source.Entries.add(new DemoEntry("Source Code of Launch View Model", LaunchCodeViewModel.class,  R.layout.code_view));
		
		Categories.add(source);
		
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
}
