package gueei.binding.markupDemov30.viewModels;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.labs.EventAggregator;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class DemoCategory {
	public final StringObservable Name = 
			new StringObservable();
	
	public final ArrayListObservable<DemoEntry> Entries = 
			new ArrayListObservable<DemoEntry>(DemoEntry.class);

	public final IntegerObservable SelectedDemoPosition = 
			new IntegerObservable(-1);
	
	private Context mContext;
	
	public DemoCategory(Context context, String name){
		Name.set(name);
		mContext = context;
	}
	
	public final Command ShowDemo = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if (args.length>0){
				DemoEntry entry = (DemoEntry)args[0];
				Bundle bundle = new Bundle();
				bundle.putString("Clazz", entry.ModelClass.get().getName());
				bundle.putInt("Layout", entry.LayoutId.get());
				EventAggregator.getInstance(mContext)
					.publish("ShowDemo", DemoCategory.this, bundle);
			}
		}
	};
	
	public void showFirstDemo(){
		SelectedDemoPosition.set(0);
		ShowDemo.Invoke(null, Entries.get(0));
	}
}
