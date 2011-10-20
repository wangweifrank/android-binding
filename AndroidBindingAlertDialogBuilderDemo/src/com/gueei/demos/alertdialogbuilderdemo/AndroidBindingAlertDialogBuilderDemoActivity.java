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

			AlertDialog.Builder builder = new AlertDialog.Builder(AndroidBindingAlertDialogBuilderDemoActivity.this);					
			BindingAlertDialogBuilderHelper helper = new BindingAlertDialogBuilderHelper();
			helper.inflateDialogView( AndroidBindingAlertDialogBuilderDemoActivity.this, R.layout.dialog_managed_buttons );
						
			AlertDialog alertDialog = helper.createAndBind(AndroidBindingAlertDialogBuilderDemoActivity.this, builder, 
							AndroidBindingAlertDialogBuilderDemoActivity.this,
							AndroidBindingAlertDialogBuilderDemoActivity.this.ManagedButtonsViewmodel.get());
			
			alertDialog.show();					
		}
	};	
	
	public final Command CustomDialogHandlingDemo = new Command(){
		public void Invoke(View view, Object... args) {
			ManagedButtonsViewmodel.get().DialogButtonInfotext.set("");

			AlertDialog.Builder  builder = new AlertDialog.Builder(AndroidBindingAlertDialogBuilderDemoActivity.this);					
			BindingAlertDialogBuilderHelper helper = new BindingAlertDialogBuilderHelper();
			helper.inflateDialogView( AndroidBindingAlertDialogBuilderDemoActivity.this, R.layout.dialog_custom );
						
			AlertDialog alertDialog = helper.createAndBind(AndroidBindingAlertDialogBuilderDemoActivity.this, builder, 
							AndroidBindingAlertDialogBuilderDemoActivity.this,
							AndroidBindingAlertDialogBuilderDemoActivity.this.CustomDialogHandlingViewmodel.get());
			
			alertDialog.show();					
		}
	};	
	
	
	public final Command ShowMessagebox = new Command(){
		public void Invoke(View view, Object... args) {
			AlertDialog.Builder builder = new AlertDialog.Builder(AndroidBindingAlertDialogBuilderDemoActivity.this);					
			BindingAlertDialogBuilderHelper helper = new BindingAlertDialogBuilderHelper();
			helper.inflateDialogView( AndroidBindingAlertDialogBuilderDemoActivity.this, R.layout.dialog_messagebox );					
			
			AlertDialog alertDialog = helper.createAndBind(AndroidBindingAlertDialogBuilderDemoActivity.this, builder,
							new MessageboxViewmodel());
			
			
			alertDialog.show();					
		}
	};
	
	
	
}