package com.gueei.android.binding.listeners;

import java.util.ArrayList;

import android.view.View;

public class OnClickListenerMulticast extends MulticastListener<View.OnClickListener> implements View.OnClickListener {
	public void onClick(View arg0) {
		this.notifyViewAttributes(arg0);
		this.invokeCommands(arg0);
	}

	@Override
	public void registerToView(View v) {
		v.setOnClickListener(this);
	}
}