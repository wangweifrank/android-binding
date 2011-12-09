package gueei.binding.markupDemov30.viewModels;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.StringObservable;

public class DemoCategory {
	public final StringObservable Name = 
			new StringObservable();
	
	public final ArrayListObservable<DemoEntry> Entries = 
			new ArrayListObservable<DemoEntry>(DemoEntry.class);
	
	public DemoCategory(String name){
		Name.set(name);
	}
}
