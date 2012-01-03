package gueei.binding.v30.viewAttributes;

import android.view.View;
import gueei.binding.Binder;
import gueei.binding.v30.listeners.OnAttachStateChangeListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class OnAttachStateChangeListenerAttributeV30 extends ViewEventAttribute<View> implements View.OnAttachStateChangeListener {
	public OnAttachStateChangeListenerAttributeV30(View view) {
		super(view, "onAttachStateChangeListener");
	}
	
	@Override
	protected void registerToListener(View view) {
		Binder.getMulticastListenerForView(view, OnAttachStateChangeListenerMulticast.class).register(this);
	}

	public void onViewAttachedToWindow(View v) {
		this.invokeCommand(v, AttachStateChange.ATTACHED);		
	}

	public void onViewDetachedFromWindow(View v) {
		this.invokeCommand(v, AttachStateChange.DETACHED);				
	}
	
}

