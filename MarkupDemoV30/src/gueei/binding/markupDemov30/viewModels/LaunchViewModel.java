package gueei.binding.markupDemov30.viewModels;

import android.content.Context;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;

public class LaunchViewModel {
	public final Observable<DemoCategory> SelectedCategory =
			new Observable<DemoCategory>(DemoCategory.class);
	
	public final ArrayListObservable<DemoCategory> Categories =
			new ArrayListObservable<DemoCategory>(DemoCategory.class);

	public final Command CategorySelected = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if (args.length==0) return;
			int index = Integer.parseInt(args[0].toString());
			if (index<Categories.size()){
				SelectedCategory.set(Categories.get(index));
			}
		}
	};
	
	public LaunchViewModel(Context context){
		DemoCategory source = new DemoCategory("Source Code");
		source.Entries.add(new DemoEntry("Markup of Launch", LaunchMarkupViewModel.class));
		source.Entries.add(new DemoEntry("Source Code of Launch View Model", LaunchMarkupViewModel.class));
		
		Categories.add(source);
	}
}
