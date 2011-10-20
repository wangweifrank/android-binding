package com.gueei.demos.alertdialogbuilderdemo.viewmodels;

import gueei.binding.observables.StringObservable;
import gueei.extensions.alertdialog.IBindingAlertDialogPositiveButtonText;
import gueei.extensions.alertdialog.IBindingAlertDialogTitleText;

public class MessageboxViewmodel implements IBindingAlertDialogPositiveButtonText, IBindingAlertDialogTitleText {	
	public final StringObservable MessageboxText = new StringObservable("simple messagebox");
		
	@Override
	public String getPositiveButtonText() {
		return "Ok";
	}

	@Override
	public String getTitleText() {
		return "A Messagebox title";
	}

}
