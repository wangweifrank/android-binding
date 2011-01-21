package com.gueei.android.binding;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.View;

public class Utility {
	private static Object getFieldForModel(String fieldName, Object model){
		try{
			Field field = model.getClass().getField(fieldName);
			return field.get(model);
		}catch(Exception e){
			return null;
		}
	}
	
	public static IObservable<?> getObservableForModel(View view, String fieldName, Object model){
		if (model instanceof IPropertyContainer){
			try{
				return ((IPropertyContainer)model).getObservableByName(fieldName);
			}catch(Exception e){
				return null;
			}
		}
		Object rawField = getFieldForModel(fieldName, model);
		if (rawField instanceof Observable<?>)
			return (IObservable<?>)rawField;
		return null;
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
	
	public static int resolveResource(String attrValue, Context context){
		if (!attrValue.startsWith("@")) return -1;
		String name = attrValue.substring(1); // remove the @ sign
		return context.getResources().getIdentifier(name, "layout", context.getPackageName());
	}
}
