package com.gueei.tutorials.calculator;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.pojo.PojoViewModelWrapper;

import android.app.Activity;
import android.os.Bundle;

public class Calculator extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binder.init(this.getApplication());
        // Use this one for POJO Model
        // Wrapper creation may change in future release
        Binder.setAndBindContentView(this, R.layout.main, 
       		PojoViewModelWrapper.create(new CalculatorPojoViewModel()));
        
        // Un-comment this line if use original view model.
        //Binder.setAndBindContentView(this, R.layout.main, new CalculatorViewModel());
    }
}