package com.gueei.demo.inputvalidation;

import com.gueei.android.binding.Binder;

import android.app.Activity;
import android.os.Bundle;

public class RegisterAccount extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binder.init();
        Binder.setAndBindContentView(this, R.layout.main, new RegistrationViewModel(this));
    }
}