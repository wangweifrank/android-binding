package com.gueei.android.binding.validation;

import com.gueei.android.binding.IObservable;

public abstract class ValidatorBase<Ta> {
	private IObservable<?> mValue;
	public abstract Class<?> getAcceptedAnnotation();
	
	public final void setObservable(IObservable<?> value){
		mValue = value;
	}
	
	/**
	 * Note to subclasses: it must have this constructor with exactly same signature.
	 * The ModelValidator will construct with this version of constructor only
	 * @param value
	 */
	public ValidatorBase(){}
	
	public final boolean Validate(Ta parameters, Object model){
		if (mValue==null) return false;
		return doValidate(mValue.get(), parameters, model);
	}
	
	protected abstract boolean doValidate(Object value, Ta parameters, Object model);
	
	public final String formatErrorMessage(Ta parameters, String fieldName){
		return doFormatErrorMessage(parameters, fieldName);
	}
	
	protected abstract String doFormatErrorMessage(Ta parameters, String fieldName);
	/**
	 * Validator should be designed in use-and-dispose manner. So if any resources it is holding, 
	 * it is advised to release it here.
	 */
	public void recycle(){}
}
