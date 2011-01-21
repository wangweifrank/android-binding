package com.gueei.android.binding.listeners;

import android.view.View;
import android.widget.CompoundButton;

public class OnCheckedChangeListenerMulticast
	extends MulticastListener<CompoundButton.OnCheckedChangeListener>
	implements CompoundButton.OnCheckedChangeListener{

	@Override
	public void registerToView(View v) {
		if (CompoundButton.class.isInstance(v)){
			((CompoundButton)v).setOnCheckedChangeListener(this);
		}
	}

	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		this.notifyViewAttributes(arg0, arg1);
	}
}
