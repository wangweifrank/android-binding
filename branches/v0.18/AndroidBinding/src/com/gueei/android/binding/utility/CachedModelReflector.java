package com.gueei.android.binding.utility;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.IObservable;
import com.gueei.android.binding.IPropertyContainer;
import com.gueei.android.binding.Observable;

public class CachedModelReflector<T>  {
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
		observables.put(".", null);
	}
	
	public Command getCommandByName(String name, T object) throws Exception {
		if (commands.containsKey(name)){
			return (Command) commands.get(name).get(object);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IObservable<Object> getObservableByName(String name, T object) throws Exception {
		if (name.equals("."))
			return new Observable(object.getClass(), object);
		if (observables.containsKey(name)){
			return (IObservable) observables.get(name).get(object);
		}
		return null;
	}

	public Object getValueByName(String name, T object) throws Exception {
		if (values.containsKey(name)){
			return values.get(name).get(object);
		}
		return null;
	}

	public Class<?> getValueTypeByName(String name){
		if (values.containsKey(name)){
			return values.get(name).getType();
		}
		return null;
	}
}
