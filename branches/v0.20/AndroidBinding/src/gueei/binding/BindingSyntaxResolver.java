package gueei.binding;

import gueei.binding.observables.StringObservable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

public class BindingSyntaxResolver {
	private static final String DEFAULT_CONVERTER_PACKAGE = "gueei.binding.converters.";
	
	private static final Pattern converterPattern = Pattern.compile("^([$a-zA-Z0-9._]+)\\((.+(\\s*?,\\s*.+)*)\\)");
	private static final Pattern dynamicObjectPattern = Pattern.compile("^\\{(.+)\\}$");
	private static final Pattern stringPattern = Pattern.compile("^'([^']*)'$");
	
	public static IObservable<?> constructObservableFromStatement(
			final Context context,
			final String bindingStatement, 
			final Object model){
		IObservable<?> result;
		String statement = bindingStatement.trim();
		result = getConverterFromStatement(context, statement, model);
		if (result!=null) return result;
		result = getDynamicObjectFromStatement(context, statement, model);
		if (result!=null) return result;
		return getObservableForModel(statement, model);
	}

	private static Converter<?> getConverterFromStatement(final Context context, String statement, Object model){
		Matcher m = converterPattern.matcher(statement);
		if ((!m.matches()) || (m.groupCount()<3))
			return null;
		
		String converterName = m.group(1);
		if (!converterName.contains(".")){
			converterName = DEFAULT_CONVERTER_PACKAGE + converterName;
		}
		try {
			String[] arguments = splitArguments(m.group(2));
			int argumentCount = arguments.length;
			IObservable<?>[] obs = new IObservable[argumentCount];
			for (int i=0; i<argumentCount; i++){
				obs[i] = constructObservableFromStatement(context, arguments[i], model);
				if (obs[i] == null){
					return null;
				}
			}
			Constructor<?> constructor = Class.forName(converterName).getConstructor(IObservable[].class);
			Converter<?> converter = (Converter<?>)constructor.newInstance((Object)(obs));
			converter.setContext(context);
			return converter;
		} catch (Exception e){
			e.printStackTrace();
			//BindingLog.warning("BindingSyntaxResolver", e.getMessage());
			return null;
		}
	}

	private static IObservable<?> getDynamicObjectFromStatement
		(final Context context, final String statement, final Object model) {
		Matcher m = dynamicObjectPattern.matcher(statement);
		if (!m.matches()) return null;
		
		DynamicObject dynamic = new DynamicObject();
		String[] arguments = splitArguments(m.group(1));
		int argumentCount = arguments.length;
		for (int i=0; i<argumentCount; i++){
			int indexOfEqual = arguments[i].indexOf('=');
			if (indexOfEqual <=0 ) return null;
			String name = arguments[i].substring(0, indexOfEqual).trim();
			String obsStatement = arguments[i].substring(indexOfEqual+1).trim();
			IObservable<?> obs = constructObservableFromStatement(context, obsStatement, model);
			if (obs == null){
				return null;
			}
			dynamic.putObservable(name, obs);
		}
		return dynamic;
	}
	
	public static String[] splitArguments(String group){
		ArrayList<String> arguments = new ArrayList<String>();
		int bracketCount = 0;
		int curlyBraceCount = 0;
		String progress = "";
		int count = group.length();
		boolean singleQuoteMode = false;
		boolean doubleQuoteMode = false;
		char[] chars = group.toCharArray();
		for(int i=0; i<count; i++){
			if (chars[i]=='\''){
				if (singleQuoteMode) singleQuoteMode=false;
				else singleQuoteMode = true;
			}
			if (chars[i]=='"'){
				if (singleQuoteMode) doubleQuoteMode=false;
				else doubleQuoteMode = true;
			}
			if (chars[i]=='(') bracketCount++;
			if (chars[i]==')') bracketCount--;
			if (chars[i]=='{') curlyBraceCount++;
			if (chars[i]=='}') curlyBraceCount--;
			if (	(chars[i] ==',') && 
					(bracketCount<=0) &&
					(curlyBraceCount<=0) &&
					(progress.length()!=0) &&
					!singleQuoteMode && 
					!doubleQuoteMode
				){
				arguments.add(progress.trim());
				progress = "";
				bracketCount=0;
				curlyBraceCount=0;
			}else{
				progress += chars[i];
			}
		}
		if ((bracketCount<=0) && (progress.length()!=0)){
			arguments.add(progress.trim());
		}
		return arguments.toArray(new String[0]);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static IObservable<?> getObservableForModel(
			String fieldName, Object model){
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
		// TODO: TEMP ONLY
		if (fieldName.startsWith("@")){
			return new ConstantObservable<Integer>(Integer.class, Utility.resolveResource(fieldName, Binder.getApplication()));
		}
		Object rawField = getFieldForModel(fieldName, model);
		if (rawField instanceof IObservable<?>)
			return (IObservable<?>)rawField;
		return null;
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
