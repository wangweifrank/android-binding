package com.gueei.demo;

import com.gueei.demo.viewmodel.TableLayoutViewmodel;

import gueei.binding.app.BindingActivity;
import android.os.Bundle;

public class BindableTableLayoutActivity extends BindingActivity {
	
	TableLayoutViewmodel vm = new TableLayoutViewmodel();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           
        this.setAndBindRootView(R.layout.bindable_table_layout, vm); 
    }
    
}