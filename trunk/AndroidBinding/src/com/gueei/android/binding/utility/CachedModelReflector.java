package com.gueei.android.binding.utility;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.IObservable;
import com.gueei.android.binding.IPropertyContainer;

public class CachedModelReflector<T> implements IPropertyContainer {
	public HashMap<String, Field> observables = new HashMap<String, Field>();
	public HashMap<String, Field> commands= new HashMap<String, Field>();
	public HashMap<String, Field> values= new HashMap<String, Field>();
	
	public CachedModelReflector(Class<T> type) throws IllegalArgumentException, IllegalAccessException{
		for (Field f: type.getFields()){
			if (IObservable.class.isAssignableFrom(f.getType())){
				observables.put(f.getName(), f);
			}
			else if (Command.class.isAssignableFrom(f.getType())){
				commands.put(f.getName(), f);
			}
			else{
				values.put(f.getName(), f);
			}
		}
	}
	
	private T mObject;
	public void setObject(T object){
		mObject = object;
	}
	
	public Command getCommandByName(String name) throws Exception {
		if (commands.containsKey(name)){
			return (Command) commands.get(name).get(mObject);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IObservable<Object> getObservableByName(String name) throws Exception {
		if (observables.containsKey(name)){
			return (IObservable) observables.get(name).get(mObject);
		}
		return null;
	}

	public Object getValueByName(String name) throws Exception {
		if (values.containsKey(name)){
			return values.get(name).get(mObject);
		}
		return null;
	}

}
