package com.gueei.demos.alertdialogbuilderdemo.viewmodels;

import android.view.View;
import gueei.binding.Command;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
import gueei.extensions.alertdialog.DialogWeakReference;
import gueei.extensions.alertdialog.IBindingAlertDialogWeakReference;

public class CustomDialogHandlingViewmodel implements IBindingAlertDialogWeakReference {
	
	private DialogWeakReference dialog = new DialogWeakReference();	
	public final StringObservable DialogInfotext = new StringObservable("text from dialog");
	public final BooleanObservable OkPressed = new BooleanObservable(false);
	

	public final Command OKCmd = new Command(){
		public void Invoke(View view, Object... args) {	
			OkPressed.set(true);
			dialog.dismiss();
		}
	};
	
	public final Command CancelCmd = new Command(){
		public void Invoke(View view, Object... args) {	
			OkPressed.set(true);
			dialog.cancel();
		}
	};

	@Override
	public DialogWeakReference getDialogDialogWeakReference() {
		return dialog;
	}	
}
