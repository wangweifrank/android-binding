package com.gueei.android.binding;

public interface Observer {
	public void onPropertyChanged(Observable<?> prop, Object newValue, Object initiator);
}
