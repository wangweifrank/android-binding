package com.gueei.android.binding.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.gueei.android.binding.IObservable;
import com.gueei.android.binding.Observable;

public class ModelValidator {
	@SuppressWarnings("unchecked")
	public static ValidationResult ValidateModel(Object model){
		ValidationResult result = new ValidationResult();
		for (Field field : model.getClass().getFields()){
			try{
				if (!(field.get(model) instanceof Observable)) continue;
				IObservable observable = (IObservable)field.get(model);
				for (Annotation annotation : field.getAnnotations()){
					Method m = annotation.getClass().getMethod("Validator");
					Class<?> validation = (Class<?>)m.invoke(annotation);
					ValidatorBase validator = (ValidatorBase)validation.getConstructor().newInstance();
					validator.setObservable(observable);
					if (!validator.Validate(validator.getAcceptedAnnotation().cast(annotation), model)){
						result.putValidationError(
								validator.formatErrorMessage(validator.getAcceptedAnnotation().cast(annotation), 
								field.getName()));
					}
					validator.recycle();
				}
			}catch(Exception e){
				// Should be impossible to get here
				continue;
			}
		}
		return result;
	}
}
