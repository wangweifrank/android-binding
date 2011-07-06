package gueei.binding.validation.validators;

import gueei.binding.validation.ValidatorBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface MinLength{
	public Class<?> Validator() default MinLengthValidator.class;
	
	public int Length();

	public String ErrorMessage() default "%fieldname% needs have at least %length% characters";
	
	public class MinLengthValidator extends ValidatorBase<MinLength> {

		@Override
		public Class<?> getAcceptedAnnotation() {
			return MinLength.class;
		}

		@Override
		protected String doFormatErrorMessage(MinLength parameters,
				String fieldName) {
			return 
				parameters.ErrorMessage()
				.replace("%fieldname%", fieldName)
				.replace("%length%", parameters.Length() + "");
		}

		@Override
		protected boolean doValidate(Object value, MinLength parameters,
				Object model) {
			if (value==null) return false;
			if (!(value instanceof CharSequence)) return true;
			return ((CharSequence)value).length() >= parameters.Length();
		}

	}
}
