package com.example.binding.demo;

import com.gueei.android.binding.Binder;

import android.app.Activity;
import android.os.Bundle;

public class Demo1Code extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Binder.setAndBindContentView(this, R.layout.demo1code, new Demo1CodeModel(this));
	}

}
