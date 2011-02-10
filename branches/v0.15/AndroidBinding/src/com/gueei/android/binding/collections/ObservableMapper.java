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

public class ObservableMapper<T> implements IPropertyContainer {
	@SuppressWarnings("rawtypes")
	public HashMap<String, MockObservable> observableMapping = new HashMap<String, MockObservable>();
	public HashMap<String, MockCommand> commandMapping = new HashMap<String, MockCommand>();
	public int mappedPosition;
	private T mappingModel;
	private CachedModelReflector<T> mReflector;

	
	// Call once and only once
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initMapping(String[] observable, String[] command, CachedModelReflector<T> reflector, T model){
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
	
	public T getCurrentMapping(){
		return mappingModel;
	}
	
	@SuppressWarnings("unchecked")
	public void changeMapping(CachedModelReflector<T> reflector, T model){
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
	private static class MockObservable<T> extends Observable<T> implements Observer{
		public MockObservable(Class<T> type) {
			super(type);
		}

		public IObservable<T> observingProperty;
		public T get() {
			if (observingProperty!=null){
				return observingProperty.get();
			}
			return null;
		}

		public void changeObservingProperty(IObservable<T> newProperty){
			if (observingProperty!=null){
				observingProperty.unsubscribe(this);
			}
			// This might not be the best idea. but so far it works :P
			/*
			for(Observer o: newProperty.getAllObservers()){
				if (o instanceof MockObservable){
					newProperty.unsubscribe(o);
				}
			}*/
			newProperty.subscribe(this);
			observingProperty = newProperty;
			this.notifyChanged(this);
		}
		
		public void onPropertyChanged(IObservable<?> prop,
			AbstractCollection<Object> initiators) {
			if (prop!=observingProperty){
				prop.unsubscribe(this);
				return;
			}
			initiators.add(this);
			this.notifyChanged(initiators);
		}

		@Override
		protected void doSetValue(T newValue,
				AbstractCollection<Object> initiators) {
			if (observingProperty!=null){
				observingProperty.set(newValue, initiators);
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