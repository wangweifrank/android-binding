package com.gueei.android.binding.viewAttributes;

import java.util.AbstractCollection;

import android.view.View;
import android.widget.AdapterView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnItemClickedListenerMulticast;

public class ClickedIdViewAttribute extends ViewAttribute<AdapterView<?>, Long> {

	private Long value;
	
	public ClickedIdViewAttribute(AdapterView<?> view, String attributeName) {
		super(view, attributeName);
		this.setReadonly(true);
		Binder.getMulticastListenerForView(view, OnItemClickedListenerMulticast.class)
			.register(this);
	}

	@Override
	public Long get() {
		return value;
	}

	@Override
	public void onAttributeChanged(View view, Object... args) {
		if (!this.view.get().equals(view)) return;
		try{
			this.value = (Long)args[2];
			super.onAttributeChanged(view, args);
		}catch(Exception e){
		}
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		// Readonly, do nothing
	}
}
