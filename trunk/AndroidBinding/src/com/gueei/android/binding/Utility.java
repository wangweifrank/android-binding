package com.gueei.android.binding;

import java.lang.reflect.Field;

import com.gueei.android.binding.adapters.ObservableCollection;

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
	
	public static Observable<?> getObservableForModel(View view, String fieldName, Object model){
		if (model instanceof ObservableCollection){
			return ((ObservableCollection)model).getObservableForName(view, fieldName);
		}
		Object rawField = getFieldForModel(fieldName, model);
		if (rawField instanceof Observable<?>)
			return (Observable<?>)rawField;
		return null;
	}

	public static Command getCommandForModel(String fieldName, Object model){
		Object rawField = getFieldForModel(fieldName, model);
		if (rawField instanceof Command)
			return (Command)rawField;
		return null;
	}
}
