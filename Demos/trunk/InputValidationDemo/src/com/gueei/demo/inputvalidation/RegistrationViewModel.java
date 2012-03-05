package com.gueei.demo.inputvalidation;

import gueei.binding.Command;
import gueei.binding.labs.validation.ModelValidator;
import gueei.binding.labs.validation.ValidationResult;
import gueei.binding.labs.validation.validators.EqualsTo;
import gueei.binding.labs.validation.validators.MinLength;
import gueei.binding.labs.validation.validators.RegexMatch;
import gueei.binding.labs.validation.validators.Required;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

public class RegistrationViewModel {
	
	@Required(ErrorMessage="Login is required")
	@RegexMatch(Pattern = "^[A-Za-z0-9_]{3,8}$", ErrorMessage="Login need to contains alphanumeric characters and must be 3-8 characters long.")
	public final StringObservable Login = new StringObservable("");

	@Required
	@MinLength(Length=5, ErrorMessageRes="validation_password_length")
	public final StringObservable Password = new StringObservable();
	
	@Required
	@EqualsTo(Observable = "Password", ErrorMessage="Confirm Password must match Password.")
	public final StringObservable ConfirmPassword = new StringObservable();
	public final Command Validate = new Command(){
		public void Invoke(View view, Object... args) {
			ToastValidationResult();
		}
	};
	
	private void ToastValidationResult(){
		long start = System.currentTimeMillis();
		ModelValidator mv = new ModelValidator(mContext, this, R.string.class);
		ValidationResult result = mv.ValidateModel();
		
		String output = "Total Time on validation: " + (System.currentTimeMillis() - start) + "ms\n";
		output += "validation result: " + result.isValid() + "\n";
		for(String msg : result.getValidationErrors()){
			output += msg + "\n";
		}
		Toast.makeText(mContext, output, Toast.LENGTH_LONG).show();
	}
	
	private final Activity mContext;
	public RegistrationViewModel(Activity context){
		mContext = context;
	}
	
	public final BooleanObservable CodeVisibleBool = new BooleanObservable(false);
	
	public final Command ToggleCode = new Command(){
		public void Invoke(View view, Object... args){
			CodeVisibleBool.toggle();
		}
	};
	
	public final Command ProjectHomepage = new Command(){
		public void Invoke(View view, Object... args){
			Uri uri = Uri.parse("http://code.google.com/p/android-binding/");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			mContext.startActivity(intent);
		}
	};
}
