package com.gueei.tutorials.calculator;

import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.serialization.ViewModelParceler;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Calculator extends BindingActivity {
	private static final String MODE = "com.gueei.tutorial.calculator.mode";
	private static final String MODEL = "com.gueei.tutorial.calculator.model";
	private Object ViewModel;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getIntent().getBooleanExtra(MODE, false)){
        	AdvancedMode.set(true);
        }

        ViewModel = new CalculatorViewModel();

        // unwrap the view model if exists
        Bundle parcel_model = getIntent().getBundleExtra(MODEL);
        if (parcel_model!=null){
        	ViewModelParceler.restoreViewModel(parcel_model, ViewModel);
        }

        // Bind the Root View with the Calculator view model
        if (AdvancedMode.get())
        	setAndBindRootView(R.layout.advanced, ViewModel);
        else
        	setAndBindRootView(R.layout.main, ViewModel);
        
        // Bind the options menu with activity
        this.setAndBindOptionsMenu(R.menu.options, this);
    }
    
    public final BooleanObservable AdvancedMode = new BooleanObservable(false);

	public final Command ToggleMode = new Command(){
		@Override
		public void Invoke(View arg0, Object... arg1) {
			// Recreate the Activity
			Intent intent = new Intent(Calculator.this, Calculator.class);
			intent.putExtra(MODE, !AdvancedMode.get());

			// Also package the view model state to the future self
			intent.putExtra(MODEL, ViewModelParceler.parcelViewModel(ViewModel));
			
			Calculator.this.finish();
			Calculator.this.startActivity(intent);
		}
    };
}