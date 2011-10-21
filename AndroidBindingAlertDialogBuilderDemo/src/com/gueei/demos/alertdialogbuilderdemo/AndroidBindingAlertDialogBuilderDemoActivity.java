package com.gueei.demos.alertdialogbuilderdemo;

import com.gueei.demos.alertdialogbuilderdemo.viewmodels.MessageboxViewmodel;
import com.gueei.demos.alertdialogbuilderdemo.viewmodels.SimpleDialogViewmodel;
import com.gueei.demos.alertdialogbuilderdemo.viewmodels.ManagedButtonsViewmodel;
import com.gueei.demos.alertdialogbuilderdemo.viewmodels.CustomDialogHandlingViewmodel;

import gueei.binding.Binder;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.app.BindingActivity;
import gueei.binding.observables.StringObservable;
import gueei.extensions.alertdialog.BindingAlertDialogBuilderHelper;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

public class AndroidBindingAlertDialogBuilderDemoActivity extends BindingActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Binder.init(this.getApplication());
		this.setAndBindRootView(R.layout.main, this);		
    }
    
	public final StringObservable ActivityInfotext = new StringObservable("text from activity");
	
	public final Observable<SimpleDialogViewmodel> SimpleDialogViewmodel 
		= new Observable<SimpleDialogViewmodel>(SimpleDialogViewmodel.class, new SimpleDialogViewmodel());	
	
	public final Observable<ManagedButtonsViewmodel> ManagedButtonsViewmodel 
		= new Observable<ManagedButtonsViewmodel>(ManagedButtonsViewmodel.class, new ManagedButtonsViewmodel());
	
	public final Observable<CustomDialogHandlingViewmodel> CustomDialogHandlingViewmodel 
		= new Observable<CustomDialogHandlingViewmodel>(CustomDialogHandlingViewmodel.class, new CustomDialogHandlingViewmodel());		
    
	public final Command SimpleDemo = new Command(){
		public void Invoke(View view, Object... args) {			
			
			// build by alert dialog builder - or use the  BindingAlertDialogBuilderHelper.bindAndShowDialog
			
			AlertDialog.Builder builder = new AlertDialog.Builder(AndroidBindingAlertDialogBuilderDemoActivity.this);					
			BindingAlertDialogBuilderHelper helper = new BindingAlertDialogBuilderHelper();
			helper.inflateDialogView( AndroidBindingAlertDialogBuilderDemoActivity.this, R.layout.dialog_simple );
						
			AlertDialog alertDialog = helper.createAndBind(AndroidBindingAlertDialogBuilderDemoActivity.this, builder, 
							AndroidBindingAlertDialogBuilderDemoActivity.this,
							AndroidBindingAlertDialogBuilderDemoActivity.this.SimpleDialogViewmodel.get());
			
			alertDialog.show();					
		}
	};
	
	public final Command ManagedButtonsDemo = new Command(){
		public void Invoke(View view, Object... args) {
			ManagedButtonsViewmodel.get().DialogButtonInfotext.set("");
						
			BindingAlertDialogBuilderHelper.bindAndShowDialog(
					AndroidBindingAlertDialogBuilderDemoActivity.this, R.layout.dialog_managed_buttons, 
					AndroidBindingAlertDialogBuilderDemoActivity.this,
					ManagedButtonsViewmodel.get() );							
		}
	};	
	
	public final Command CustomDialogHandlingDemo = new Command(){
		public void Invoke(View view, Object... args) {
			ManagedButtonsViewmodel.get().DialogButtonInfotext.set("");
			
			BindingAlertDialogBuilderHelper.bindAndShowDialog(
					AndroidBindingAlertDialogBuilderDemoActivity.this, R.layout.dialog_custom, 
					AndroidBindingAlertDialogBuilderDemoActivity.this,
					CustomDialogHandlingViewmodel.get() );								
		}
	};	
	
	
	public final Command ShowMessagebox = new Command(){
		public void Invoke(View view, Object... args) {			
			BindingAlertDialogBuilderHelper.bindAndShowDialog(
					AndroidBindingAlertDialogBuilderDemoActivity.this, R.layout.dialog_messagebox, 
					new MessageboxViewmodel() );				
		}
	};
	
	public final Command ShowCustomStyle = new Command(){
		public void Invoke(View view, Object... args) {			
			BindingAlertDialogBuilderHelper.bindAndShowDialog(
					AndroidBindingAlertDialogBuilderDemoActivity.this, 
					R.style.AlertDialogCustom, R.layout.dialog_messagebox, 
					new MessageboxViewmodel() );				
		}
	};
	
	
}