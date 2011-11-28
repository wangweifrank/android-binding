package com.gueei.demo;

import com.gueei.demo.viewmodel.LinearLayoutViewmodel;

import gueei.binding.app.BindingActivity;
import android.os.Bundle;

public class BindableLinearLayoutActivity extends BindingActivity {
	
	LinearLayoutViewmodel vm = new LinearLayoutViewmodel();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           
        this.setAndBindRootView(R.layout.bindable_linear_layout, vm); 
    }
    
}