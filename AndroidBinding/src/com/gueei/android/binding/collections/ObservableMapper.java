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

public class ObservableMapper implements IPropertyContainer {
	public HashMap<String, MockObservable> observableMapping = new HashMap<String, MockObservable>();
	public HashMap<String, MockCommand> commandMapping = new HashMap<String, MockCommand>();
	public int mappedPosition;
	
	// Call once and only once
	public void initMapping(String[] observable, String[] command){
		int numObservables = observable.length;
		for(int i=0; i<numObservables; i++){
			observableMapping.put(observable[i], new MockObservable());
		}
		int numCommands = command.length;
		for(int i=0; i<numCommands; i++){
			commandMapping.put(command[i], new MockCommand());
		}
	}
	
	public <T> void changeMapping(CachedModelReflector<T> reflector, T model){
		try {
			reflector.setObject(model);
			for(String key: observableMapping.keySet()){
					observableMapping.get(key).changeObservingProperty(reflector.getObservableByName(key));
			}
			for(String key: commandMapping.keySet()){
				commandMapping.get(key).changeCommand(reflector.getCommandByName(key));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Remember! This maps 1-1 to the real observable
	private class MockObservable extends Observable<Object> implements Observer{
		private WeakReference<IObservable<Object>> observingProperty = new WeakReference<IObservable<Object>>(null);
		public Object get() {
			if (observingProperty.get()!=null){
				return observingProperty.get().get();
			}
			return null;
		}

		public void changeObservingProperty(IObservable<Object> newProperty){
			if (observingProperty.get()!=null){
				observingProperty.get().unsubscribe(this);
			}
			newProperty.subscribe(this);
			observingProperty = new WeakReference<IObservable<Object>>(newProperty);
			this.notifyChanged(this);
		}
		
		public void onPropertyChanged(IObservable<?> prop,
			AbstractCollection<Object> initiators) {
			if (prop!=observingProperty.get()) return;
			initiators.add(this);
			this.notifyChanged(initiators);
		}

		@Override
		protected void doSetValue(Object newValue,
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

	public Object getValueByName(String name) {
		return null;
	}
}