package com.gueei.android.binding.viewAttributes;


import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnCheckedChangeListenerMulticast;

public class CheckedViewAttribute extends ViewAttribute<CompoundButton, Boolean>
	implements OnCheckedChangeListener{

	public CheckedViewAttribute(CompoundButton view) {
		super(Boolean.class, view, "checked");
		Binder.getMulticastListenerForView(view, OnCheckedChangeListenerMulticast.class)
			.register(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		boolean changeTo = getView().isChecked();
		if (newValue==null){
			changeTo = false;
		}
		if (newValue instanceof Boolean){
			changeTo = (Boolean)newValue;
		}
		if (newValue instanceof Number){
			changeTo = !((Number)newValue).equals(0);
		}
		if (changeTo != getView().isChecked()){
			Binder.getMulticastListenerForView(getView(), OnCheckedChangeListenerMulticast.class).nextActionIsNotFromUser();
			getView().setChecked(changeTo);
		}
	}

	@Override
	public Boolean get() {
		return getView().isChecked();
	}

	public void onCheckedChanged(CompoundButton view, boolean checked) {
		this.notifyChanged();
	}
}