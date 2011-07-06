package gueei.binding.validation.validators;

import gueei.binding.validation.ValidatorBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface MaxLength{
	public Class<?> Validator() default MaxLengthValidator.class;
	
	public int Length();

	public String ErrorMessage() default "%fieldname% cannot have more than %length% characters";
	
	public class MaxLengthValidator extends ValidatorBase<MaxLength> {

		@Override
		public Class<?> getAcceptedAnnotation() {
			return MaxLength.class;
		}

		@Override
		protected String doFormatErrorMessage(MaxLength parameters,
				String fieldName) {
			return 
				parameters.ErrorMessage()
				.replace("%fieldname%", fieldName)
				.replace("%length%", parameters.Length() + "");
		}

		@Override
		protected boolean doValidate(Object value, MaxLength parameters,
				Object model) {
			if (value==null) return false;
			if (!(value instanceof CharSequence)) return true;
			return ((CharSequence)value).length() <= parameters.Length();
		}

	}
}
