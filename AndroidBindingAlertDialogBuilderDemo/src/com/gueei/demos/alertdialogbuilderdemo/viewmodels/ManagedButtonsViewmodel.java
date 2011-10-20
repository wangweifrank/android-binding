package com.gueei.demos.alertdialogbuilderdemo.viewmodels;

import gueei.binding.observables.StringObservable;
import gueei.extensions.alertdialog.IBindingAlertDialogCancel;
import gueei.extensions.alertdialog.IBindingAlertDialogNegativeButtonHandler;
import gueei.extensions.alertdialog.IBindingAlertDialogNeutralButtonHandler;
import gueei.extensions.alertdialog.IBindingAlertDialogPositiveButtonHandler;

public class ManagedButtonsViewmodel 
		implements IBindingAlertDialogPositiveButtonHandler, 
				   IBindingAlertDialogNeutralButtonHandler, 
				   IBindingAlertDialogNegativeButtonHandler,
				   IBindingAlertDialogCancel {	
	public final StringObservable DialogInfotext = new StringObservable("text from dialog");
	public final StringObservable DialogButtonInfotext = new StringObservable("");

	@Override
	public void onPositiveButtonPressed() {	
		DialogButtonInfotext.set("onPositiveButtonPressed");
	}

	@Override
	public String getPositiveButtonText() {
		return "Positive";
	}

	@Override
	public void onNeutralButtonPressed() {
		DialogButtonInfotext.set("onNeutralButtonPressed");
	}

	@Override
	public String getNeutralButtonText() {
		return "Neutral";
	}

	@Override
	public void onNegativeButtonPressed() {		
		DialogButtonInfotext.set("onNegativeButtonPressed");
	}

	@Override
	public String getNegativeButtonText() {
		return "Negative";
	}

	@Override
	public void onCancel() {
		DialogButtonInfotext.set("onCancel");
	}	
}
