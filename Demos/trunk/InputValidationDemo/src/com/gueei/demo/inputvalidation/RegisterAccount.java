package com.gueei.demo.inputvalidation;

import gueei.binding.app.BindingActivity;
import android.os.Bundle;

public class RegisterAccount extends BindingActivity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setAndBindRootView(R.layout.main, new RegistrationViewModel(this));
    }
}