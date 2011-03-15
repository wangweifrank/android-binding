package com.gueei.android.binding.listeners;

import android.view.KeyEvent;
import android.view.View;

public class OnKeyListenerMulticast extends MulticastListener<View.OnKeyListener> implements View.OnKeyListener {
	@Override
	public void registerToView(View v) {
		v.setOnKeyListener(this);
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		for (View.OnKeyListener l: listeners){
			l.onKey(v, keyCode, event);
		}

		return false;
	}
}