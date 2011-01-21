package com.gueei.android.binding.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnItemClickedListenerMulticast extends MulticastListener<OnItemClickListener>
		implements OnItemClickListener {

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		this.notifyViewAttributes(arg0, arg1, arg2, arg3);
		this.invokeCommands(arg0, arg1, arg2, arg3);
	}

	@Override
	public void registerToView(View v) {
		if (!(v instanceof AdapterView<?>)) return;
		((AdapterView<?>)v).setOnItemClickListener(this);
	}

}
