package gueei.binding.markupDemov30.viewModels;

import gueei.binding.Observable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class DemoEntry {
	public final StringObservable Name = 
			new StringObservable();
	
	public final IntegerObservable LayoutId =
			new IntegerObservable();
	public final Observable<Class> ModelClass = 
			new Observable<Class>(Class.class);
	
	public DemoEntry(String name, Class<?> clazz, int layoutId){
		Name.set(name);
		ModelClass.set(clazz);
		LayoutId.set(layoutId);
	}
}
