package com.gueei.demo.inputvalidation;

import java.util.AbstractCollection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.validation.ModelValidator;
import com.gueei.android.binding.validation.ValidationResult;
import com.gueei.android.binding.validation.validators.EqualsTo;
import com.gueei.android.binding.validation.validators.RegexMatch;
import com.gueei.android.binding.validation.validators.Required;

public class RegistrationViewModel {
	
	@Required(ErrorMessage="Login is required")
	@RegexMatch(Pattern = "^[A-Za-z0-9_]{3,8}$"
		, ErrorMessage="Login need to contains alphanumeric characters and must be 3-8 characters long.")
	public final Observable<CharSequence> Login = new Observable<CharSequence>("");

	@Required
	public final Observable<CharSequence> Password = new Observable<CharSequence>();
	
	@Required
	@EqualsTo(Observable = "Password", ErrorMessage="Confirm Password must match Password.")
	public final Observable<CharSequence> ConfirmPassword = new Observable<CharSequence>();
	public final Command Validate = new Command(){
		public void Invoke(View view, Object... args) {
			ToastValidationResult();
		}
	};
	
	private void ToastValidationResult(){
		String output = "";
		ValidationResult result = ModelValidator.ValidateModel(this);
		output = "validation result: " + result.isValid() + "\n";
		for(String msg : result.getValidationErrors()){
			output += msg + "\n";
		}
		Toast.makeText(mContext, output, Toast.LENGTH_LONG).show();
	}
	
	private final Activity mContext;
	public RegistrationViewModel(Activity context){
		mContext = context;
	}
	
	public final Observable<Boolean> CodeVisibleBool = new Observable<Boolean>(false);
	
	public final DependentObservable<Integer> CodeVisible = 
		new DependentObservable<Integer>(CodeVisibleBool){
			@Override
			public Integer calculateValue(Object... args) {
				if ((Boolean)args[0]){
					return View.VISIBLE;
				}
				return View.GONE;
			}
	};
	
	public final DependentObservable<String> TextToggle = 
		new DependentObservable<String>(CodeVisibleBool){
			@Override
			public String calculateValue(Object... args) {
				if ((Boolean)args[0]){
					return "Hide";
				}
				return "What's THIS?";
			}
	};
	
	public final Command ToggleCode = new Command(){
		public void Invoke(View view, Object... args){
			CodeVisibleBool.set(!CodeVisibleBool.get());
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
