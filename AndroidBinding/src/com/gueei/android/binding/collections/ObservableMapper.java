package com.gueei.android.binding.collections;

import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.HashMap;

import android.view.View;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.IObservable;
import com.gueei.android.binding.IPropertyContainer;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.Observer;
import com.gueei.android.binding.utility.CachedModelReflector;

class ObservableMapper implements IPropertyContainer {
	@SuppressWarnings("rawtypes")
	public HashMap<String, MockObservable> observableMapping = new HashMap<String, MockObservable>();
	public HashMap<String, MockCommand> commandMapping = new HashMap<String, MockCommand>();
	public int mappedPosition;
	private Object mappingModel;
	private CachedModelReflector mReflector;

	
	// Call once and only once
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> void initMapping(String[] observable, String[] command, CachedModelReflector<T> reflector, T model){
		int numObservables = observable.length;
		mappingModel = model;
		mReflector = reflector;
		for(int i=0; i<numObservables; i++){
			try {
				IObservable<?> obs = reflector.getObservableByName(observable[i], model);
				observableMapping.put(observable[i], new MockObservable(obs.getType()));
			} catch (Exception e) {
			}
		}
		int numCommands = command.length;
		for(int i=0; i<numCommands; i++){
			commandMapping.put(command[i], new MockCommand());
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> void changeMapping(CachedModelReflector<T> reflector, T model){
		mappingModel = model;
		try {
			for(String key: observableMapping.keySet()){
				observableMapping.get(key).changeObservingProperty(reflector.getObservableByName(key, model));
			}
			for(String key: commandMapping.keySet()){
				commandMapping.get(key).changeCommand(reflector.getCommandByName(key, model));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Remember! This maps 1-1 to the real observable
	private class MockObservable<T> extends Observable<T> implements Observer{
		public MockObservable(Class<T> type) {
			super(type);
		}

		private WeakReference<IObservable<T>> observingProperty = new WeakReference<IObservable<T>>(null);
		public T get() {
			if (observingProperty.get()!=null){
				return observingProperty.get().get();
			}
			return null;
		}

		public void changeObservingProperty(IObservable<T> newProperty){
			if (observingProperty.get()!=null){
				observingProperty.get().unsubscribe(this);
			}
			newProperty.subscribe(this);
			observingProperty = new WeakReference<IObservable<T>>(newProperty);
			this.notifyChanged(this);
		}
		
		public void onPropertyChanged(IObservable<?> prop,
			AbstractCollection<Object> initiators) {
			if (prop!=observingProperty.get()) return;
			initiators.add(this);
			this.notifyChanged(initiators);
		}

		@Override
		protected void doSetValue(T newValue,
				AbstractCollection<Object> initiators) {
			if (observingProperty.get()!=null){
				observingProperty.get().set(newValue, initiators);
			}
		}
	}

	private class MockCommand implements Command{
		private WeakReference<Command> command;
		public void Invoke(View view, Object... args) {
			if (command.get()!=null){
				command.get().Invoke(view, args);
			}
		}
		public void changeCommand(Command newCommand){
			command = new WeakReference<Command>(newCommand);
		}
	}

	
	public Command getCommandByName(String name) {
		return commandMapping.get(name);
	}

	public IObservable<?> getObservableByName(String name) {
		return observableMapping.get(name);
	}

	public Object getValueByName(String name) throws Exception {
		if ((mappingModel==null)||(mReflector==null)) return null;
		return mReflector.getValueByName(name, mappingModel);
	}
}