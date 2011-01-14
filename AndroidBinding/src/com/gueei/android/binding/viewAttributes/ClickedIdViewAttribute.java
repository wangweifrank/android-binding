package com.gueei.android.binding.viewAttributes;

import java.util.AbstractCollection;

import android.view.View;
import android.widget.AdapterView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnItemClickedListenerMulticast;

public class ClickedIdViewAttribute extends ViewAttribute<AdapterView, Long> {

	private Long value;
	
	public ClickedIdViewAttribute(AdapterView view, String attributeName) {
		super(view, attributeName);
		this.setReadonly(true);
		Binder.getMulticastListenerForView(view, OnItemClickedListenerMulticast.class)
			.register(this);
	}

	@Override
	protected void doSet(Long newValue) {
		// do nothing. this is a readonly attribute
	}

	@Override
	public Long get() {
		return value;
	}

	@Override
	public void Invoke(View view, Object... args) {
		if (!this.view.get().equals(view)) return;
		try{
			this.value = (Long)args[2];
			super.Invoke(view, args);
		}catch(Exception e){
		}
	}
}
