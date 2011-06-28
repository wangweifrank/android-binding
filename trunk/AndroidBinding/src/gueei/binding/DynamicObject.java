package gueei.binding;

import java.util.WeakHashMap;

public class DynamicObject extends Observable<DynamicObject> implements IPropertyContainer{
	public DynamicObject() {
		super(DynamicObject.class);
	}

	private WeakHashMap<String, IObservable<?>> observables = 
		 new WeakHashMap<String, IObservable<?>>();
	
	public void putObservable(String name, IObservable<?> observable){
		observables.put(name, observable);
	}
	
	public boolean observableExists(String name){
		return observables.containsKey(name);
	}
	
	public IObservable<?> getObservableByName(String name) throws Exception {
		return observables.get(name);
	}

	public Command getCommandByName(String name) throws Exception {
		return null;
	}

	@Override
	public DynamicObject get() {
		return this;
	}

	public Object getValueByName(String name) throws Exception {
		return null;
	}
}
