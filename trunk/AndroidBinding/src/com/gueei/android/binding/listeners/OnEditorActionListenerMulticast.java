package com.gueei.android.binding.listeners;

import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

public class OnEditorActionListenerMulticast
	extends MulticastListener<TextView.OnEditorActionListener>
	implements TextView.OnEditorActionListener{

	@Override
	public void registerToView(View v) {
		if (TextView.class.isInstance(v)){
			((TextView)v).setOnEditorActionListener(this);
		}
	}

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		this.notifyViewAttributes(v, actionId, event);
		return false;
	}
}
