package gueei.binding.validation.validators;

import gueei.binding.validation.ValidatorBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import gueei.binding.R;

@Retention(RetentionPolicy.RUNTIME)
public @interface Required{
	public Class<?> Validator() default RequiredValidator.class;

	public String ErrorMessage() default "";
	public int ErrorMessagRes() default R.string.validation_required_message;
	
	public class RequiredValidator extends ValidatorBase<Required> {

		@Override
		public Class<?> getAcceptedAnnotation() {
			return Required.class;
		}

		@Override
		protected String doFormatErrorMessage(Required parameters,
				String fieldName) {
			return parameters.ErrorMessage().replace("%fieldname%", fieldName);
		}

		@Override
		protected boolean doValidate(Object value, Required parameters,
				Object model) {
			if (value==null) return false;
			if (Boolean.FALSE.equals(value)) return false;
			if (value instanceof CharSequence){
				if (((CharSequence) value).length() == 0) return false;
			}
			return true;
		}

	}
}
