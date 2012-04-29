package gueei.binding;

import java.util.Collection;
import java.util.HashMap;

public class DynamicObject extends Observable<DynamicObject> implements IPropertyContainer, Observer{
	public DynamicObject() {
		super(DynamicObject.class);
	}

	private HashMap<String, IObservable<?>> observables = 
		 new HashMap<String, IObservable<?>>();
	
	public void putObservable(String name, IObservable<?> observable){
		observables.put(name, observable);
		observable.subscribe(this);
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

	@Override
	public void onPropertyChanged(IObservable<?> prop,
			Collection<Object> initiators) {
		this.notifyChanged(initiators);
	}
}
