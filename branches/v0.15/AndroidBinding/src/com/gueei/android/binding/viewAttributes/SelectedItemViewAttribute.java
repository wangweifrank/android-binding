package com.gueei.android.binding.viewAttributes;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingType;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnItemSelectedListenerMulticast;

public class SelectedItemViewAttribute extends ViewAttribute<AdapterView<?>, Object>
	implements OnItemSelectedListener{

	public SelectedItemViewAttribute(AdapterView<?> view, String attributeName) {
		super(Object.class, view, attributeName);
		this.setReadonly(true);
		Binder.getMulticastListenerForView(view, OnItemSelectedListenerMulticast.class)
			.register(this);
	}

	@Override
	public Object get() {
		return getView().getSelectedItem();
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		// Readonly, do nothing
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		this.notifyChanged();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		this.notifyChanged();
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.TwoWay;
	}
}
