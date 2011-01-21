package com.gueei.android.binding.listeners;

import java.util.ArrayList;

import android.view.KeyEvent;
import android.view.View;

public class OnKeyListenerMulticast extends MulticastListener<View.OnKeyListener> implements View.OnKeyListener {
	@Override
	public void registerToView(View v) {
		v.setOnKeyListener(this);
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		this.notifyViewAttributes(v, keyCode, event);
		return false;
	}
}