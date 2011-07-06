package gueei.binding.validation.validators;

import gueei.binding.validation.ValidatorBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;


@Retention(RetentionPolicy.RUNTIME)
public @interface RegexMatch{
	public Class<?> Validator() default RegexMatchValidator.class;

	public String Pattern();
	public String ErrorMessage() default "%fieldname% does not match the regext pattern: %pattern%";
	
	public class RegexMatchValidator extends ValidatorBase<RegexMatch> {

		@Override
		public Class<?> getAcceptedAnnotation() {
			return RegexMatch.class;
		}

		@Override
		protected String doFormatErrorMessage(RegexMatch parameters,
				String fieldName) {
			return parameters.ErrorMessage()
				.replace("%fieldname%", fieldName)
				.replace("%pattern%", parameters.Pattern());
		}

		@Override
		protected boolean doValidate(Object value, RegexMatch parameters,
				Object model) {
			// That do not violate with required field
			if (value==null) return true;
			if (value instanceof CharSequence){
				return Pattern.matches(parameters.Pattern(), ((CharSequence)value));
			}
			// if not CharSequence, just return true
			return true;
		}

	}
}
