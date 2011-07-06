package gueei.binding.viewAttributes.view;

import android.view.View;
import gueei.binding.Binder;
import gueei.binding.listeners.OnClickListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class OnClickViewEvent extends ViewEventAttribute<View> implements View.OnClickListener {
	public OnClickViewEvent(View view) {
		super(view, "onClick");
	}

	public void onClick(View v) {
		invokeCommand(v);
	}

	@Override
	protected void registerToListener(View view) {
		Binder.getMulticastListenerForView(view, OnClickListenerMulticast.class).register(this);
	}
}