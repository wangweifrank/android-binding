package gueei.binding.viewAttributes.view;

import android.view.View;
import gueei.binding.Binder;
import gueei.binding.listeners.OnLongClickListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class OnLongClickViewEvent extends ViewEventAttribute<View> implements View.OnLongClickListener {

	public OnLongClickViewEvent(View view) {
		super(view, "onLongClick");
	}

	public boolean onLongClick(View v) {
		this.invokeCommand(v);
		return true;
	}

	@Override
	protected void registerToListener(View view) {
		Binder.getMulticastListenerForView(view, OnLongClickListenerMulticast.class).register(this);
	}
}
