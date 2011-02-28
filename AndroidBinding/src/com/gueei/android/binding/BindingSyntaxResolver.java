package com.gueei.android.binding;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BindingSyntaxResolver {
	private static Pattern converterPattern = Pattern.compile("^([a-zA-Z0-9._]+)\\([a-zA-Z0-9_]+(\\s?,\\s?[a-zA-Z0-9_]+)?\\)");
	public static IObservable<?> constructObservableFromStatement(String bindingStatement, Object model){
		Matcher m = converterPattern.matcher(bindingStatement);
		if ((!m.matches()) || (m.groupCount()<3))
			return getObservableForModel(bindingStatement, model);
		
		String converterName = m.group(1);
		try {
			Constructor<?> constructor = Class.forName(converterName).getConstructor(IObservable[].class);
			String[] arguments = m.group(2).split("\\s?,\\s");
			int argumentCount = arguments.length;
			IObservable<?>[] obs = new IObservable<?>[argumentCount];
			for (int i=0; i<argumentCount; i++){
				obs[i] = getObservableForModel(arguments[i], model);
				if (obs[i] == null){
					return getObservableForModel(bindingStatement, model);
				}
			}
			return (DependentObservable<?>)constructor.newInstance((Object[])obs);
		} catch (Exception e){
			return getObservableForModel(bindingStatement, model);
		}
	}
	
	private static IObservable<?> getObservableForModel(String fieldName, Object model){
		if (model instanceof IPropertyContainer){
			try{
				return ((IPropertyContainer)model).getObservableByName(fieldName);
			}catch(Exception e){
				return null;
			}
		}
		Object rawField = getFieldForModel(fieldName, model);
		if (rawField instanceof Observable<?>)
			return (Observable<?>)rawField;
		return null;
	}
	
	private static Object getFieldForModel(String fieldName, Object model){
		try{
			if (model instanceof IPropertyContainer){
				return ((IPropertyContainer)model).getValueByName(fieldName);
			}
			Field field = model.getClass().getField(fieldName);
			return field.get(model);
		}catch(Exception e){
			return null;
		}
	}
}
