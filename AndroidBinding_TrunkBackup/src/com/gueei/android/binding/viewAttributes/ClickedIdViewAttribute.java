package com.gueei.android.binding.viewAttributes;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnItemClickListenerMulticast;

public class ClickedIdViewAttribute extends ViewAttribute<AdapterView<?>, Long>
	implements OnItemClickListener{

	private Long value;
	
	public ClickedIdViewAttribute(AdapterView<?> view, String attributeName) {
		super(Long.class, view, attributeName);
		this.setReadonly(true);
		Binder.getMulticastListenerForView(view, OnItemClickListenerMulticast.class)
			.register(this);
	}

	@Override
	public Long get() {
		return value;
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		// Readonly, do nothing
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (!getView().equals(parent)) return;
		this.value = id;
		this.notifyChanged();
	}
}
