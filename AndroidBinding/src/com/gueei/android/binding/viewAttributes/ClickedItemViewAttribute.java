package com.gueei.android.binding.viewAttributes;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnItemClickListenerMulticast;

public class ClickedItemViewAttribute extends ViewAttribute<AdapterView<?>, Object>
	implements OnItemClickListener{

	private Object value;
	
	public ClickedItemViewAttribute(AdapterView<?> view, String attributeName) {
		super(Object.class, view, attributeName);
		this.setReadonly(true);
		Binder.getMulticastListenerForView(view, OnItemClickListenerMulticast.class)
			.register(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		// do nothing. this is a readonly attribute
	}

	@Override
	public Object get() {
		return value;
	}


	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (!getView().equals(parent)) return;
		try{
			this.value = getView().getItemAtPosition(position);
			this.notifyChanged();
		}catch(Exception e){
		}
	}
	
}
