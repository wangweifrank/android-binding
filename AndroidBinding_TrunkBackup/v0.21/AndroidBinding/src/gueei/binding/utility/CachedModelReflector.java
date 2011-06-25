package gueei.binding.utility;

import gueei.binding.Command;
import gueei.binding.IObservable;
import gueei.binding.Observable;

import java.lang.reflect.Field;
import java.util.HashMap;


public class CachedModelReflector<T> implements ICachedModelReflector<T> {
	private HashMap<String, Field> observables = new HashMap<String, Field>();
	private HashMap<String, Field> commands= new HashMap<String, Field>();
	private HashMap<String, Field> values= new HashMap<String, Field>();
	
	/* (non-Javadoc)
	 * @see gueei.binding.utility.ICachedModelReflector#getObservables()
	 */
	public HashMap<String, Field> getObservables() {
		return observables;
	}

	/* (non-Javadoc)
	 * @see gueei.binding.utility.ICachedModelReflector#getCommands()
	 */
	public HashMap<String, Field> getCommands() {
		return commands;
	}

	/* (non-Javadoc)
	 * @see gueei.binding.utility.ICachedModelReflector#getValues()
	 */
	public HashMap<String, Field> getValues() {
		return values;
	}

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
	
	/* (non-Javadoc)
	 * @see gueei.binding.utility.ICachedModelReflector#getCommandByName(java.lang.String, T)
	 */
	public Command getCommandByName(String name, Object object) throws Exception {
		if (commands.containsKey(name)){
			return (Command) commands.get(name).get(object);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see gueei.binding.utility.ICachedModelReflector#getObservableByName(java.lang.String, T)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IObservable<Object> getObservableByName(String name, Object object) throws Exception {
		if (name.equals("."))
			return new Observable(object.getClass(), object);
		if (observables.containsKey(name)){
			return (IObservable) observables.get(name).get(object);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see gueei.binding.utility.ICachedModelReflector#getValueByName(java.lang.String, T)
	 */
	public Object getValueByName(String name, Object object) throws Exception {
		if (values.containsKey(name)){
			return values.get(name).get(object);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see gueei.binding.utility.ICachedModelReflector#getValueTypeByName(java.lang.String)
	 */
	public Class<?> getValueTypeByName(String name){
		if (values.containsKey(name)){
			return values.get(name).getType();
		}
		return null;
	}
}
