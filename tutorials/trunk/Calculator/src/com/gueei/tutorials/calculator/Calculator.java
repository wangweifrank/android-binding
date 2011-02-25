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
        Binder.setAndBindContentView(this, R.layout.main, 
       		PojoViewModelWrapper.create(new CalculatorPojoViewModel()));
    }
}