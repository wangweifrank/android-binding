package com.gueei.android.binding.listeners;

import java.util.ArrayList;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

public class OnItemSelectedListenerMulticast 
	extends MulticastListener<AdapterView.OnItemSelectedListener> 
	implements AdapterView.OnItemSelectedListener {

	@Override
	public void registerToView(View v) {
		if (!(v instanceof AdapterView<?>)) return;
		((AdapterView<?>)v).setOnItemSelectedListener(this);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		this.notifyViewAttributes(arg0, arg1, arg2, arg3);
		this.invokeCommands(arg0, arg1, arg2, arg3);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		this.notifyViewAttributes(arg0);
		this.invokeCommands(arg0);
	}
}