package gueei.binding;

import java.lang.reflect.Field;

import android.content.Context;

public class Utility {
	public static Object getFieldForModel(String fieldName, Object model){
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
	
	public static IObservable<?> getObservableForModel(Context context, String fieldName, Object model){
		return BindingSyntaxResolver.constructObservableFromStatement(context, fieldName, model);
	}

	public static Command getCommandForModel(String fieldName, Object model){
		if (model instanceof IPropertyContainer){
			try{
				return ((IPropertyContainer)model).getCommandByName(fieldName);
			}catch(Exception e){
				return null;
			}
		}
		Object rawField = getFieldForModel(fieldName, model);
		if (rawField instanceof Command)
			return (Command)rawField;
		return null;
	}
	
	public static int resolveLayoutResource(String attrValue, Context context){
		if (!attrValue.startsWith("@")) return -1;
		String name = attrValue.substring(1); // remove the @ sign
		return context.getResources().getIdentifier(name, "layout", context.getPackageName());
	}
	
	public static int resolveResourceId(String attrValue, Context context, String type){
		if (!attrValue.startsWith("@")) return -1;
		String name = attrValue.substring(1); // remove the @ sign
		return context.getResources().getIdentifier(name, type, context.getPackageName());
	}
}
