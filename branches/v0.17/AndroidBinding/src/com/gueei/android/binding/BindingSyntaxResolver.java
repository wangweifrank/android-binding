package com.gueei.android.binding;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gueei.android.binding.observables.DoubleObservable;
import com.gueei.android.binding.observables.StringObservable;

public class BindingSyntaxResolver {
	private static final String DEFAULT_CONVERTER_PACKAGE = "com.gueei.android.binding.converters.";
	
	private static Pattern converterPattern = Pattern.compile("^([a-zA-Z0-9._]+)\\((.+(\\s*?,\\s*.+)*)\\)");
	private static Pattern stringPattern = Pattern.compile("^'([^']*)'$");
	
	public static IObservable<?> constructObservableFromStatement(String bindingStatement, Object model){
		Matcher m = converterPattern.matcher(bindingStatement);
		if ((!m.matches()) || (m.groupCount()<3))
			return getObservableForModel(bindingStatement, model);
		
		String converterName = m.group(1);
		if (!converterName.contains(".")){
			converterName = DEFAULT_CONVERTER_PACKAGE + converterName;
		}
		try {
			Constructor<?> constructor = Class.forName(converterName).getConstructor(IObservable[].class);
			String[] arguments = m.group(2).split("\\s*,\\s*");
			int argumentCount = arguments.length;
			IObservable[] obs = new IObservable[argumentCount];
			for (int i=0; i<argumentCount; i++){
				obs[i] = constructObservableFromStatement(arguments[i], model);
				if (obs[i] == null){
					return null;
				}
			}
			return (DependentObservable<?>)constructor.newInstance((Object)(obs));
		} catch (Exception e){
			e.printStackTrace();
			//BindingLog.warning("BindingSyntaxResolver", e.getMessage());
			return getObservableForModel(bindingStatement, model);
		}
	}
	
	private static String[] splitArguments(String group){
		ArrayList<String> arguments = new ArrayList<String>();
		int bracketCount = 0;
		String progress = "";
		int count = group.length();
		char[] chars = group.toCharArray();
		for(int i=0; i<count; i++){
			if (chars[i]=='(') bracketCount++;
			if (chars[i]==')') bracketCount--;
			if ((chars[i]==' ') || (chars[i] ==',')){
				if ((bracketCount<=0) && (progress.length()!=0)){
					arguments.add(progress);
					progress = "";
					bracketCount=0;
				}
			}else{
				progress += chars[i];
			}
		}
		return arguments.toArray(new String[0]);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static IObservable<?> getObservableForModel(String fieldName, Object model){
		IObservable<?> result = matchString(fieldName);
		if (result!=null) return result;
		
		if (model instanceof IPropertyContainer){
			try{
				return ((IPropertyContainer)model).getObservableByName(fieldName);
			}catch(Exception e){
				return null;
			}
		}
		if (fieldName.equals(".")){
			return new Observable(model.getClass(), model);
		}
		Object rawField = getFieldForModel(fieldName, model);
		if (rawField instanceof Observable<?>)
			return (Observable<?>)rawField;
		return new ConstantObservable<String>(String.class, "!!Error in resolving " + fieldName);
	}
	
	private static IObservable<?> matchString(String fieldName){
		Matcher m = stringPattern.matcher(fieldName);
		if (!m.matches()) return null;
		return new StringObservable(m.group(1));
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
