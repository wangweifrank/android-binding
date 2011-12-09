package gueei.binding.markupDemov30.viewModels;

import gueei.binding.Observable;
import gueei.binding.observables.StringObservable;

public class DemoEntry {
	public final StringObservable Name = 
			new StringObservable();
	
	public final Observable<Class> ModelClass = 
			new Observable<Class>(Class.class);
	
	public DemoEntry(String name, Class<?> clazz){
		Name.set(name);
		ModelClass.set(clazz);
	}
}
