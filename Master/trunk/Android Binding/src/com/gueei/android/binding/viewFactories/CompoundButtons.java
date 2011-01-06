package com.gueei.android.binding.viewFactories;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gueei.android.binding.BindedView;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.listeners.MulticastListener;
import com.gueei.android.binding.listeners.OnCheckedChangeListenerMulticast;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;

public class CompoundButtons implements BindingViewFactory {

	public View CreateView(String name, Binder binder, Context context,
			AttributeSet attrs) {
		return null;
	}

	public void Init() {
		MulticastListener.Factory.RegisterConstructor
			(CompoundButton.OnCheckedChangeListener.class, 
					OnCheckedChangeListenerMulticast.class);
	}

	@SuppressWarnings("unchecked")
	public boolean BindView(BindedView view, Binder binder, ViewFactory.AttributeMap attrs,
			Object model) {
		try {
			if(!CompoundButton.class.isInstance(view.getView())) return false;
			if (attrs.containsKey("checked")){
				Field f = model.getClass().getField(attrs.get("checked"));
				Observable<Boolean> prop = (Observable<Boolean>) f.get(model);
				binder.bind(view.getView(), "Checked",
						CompoundButton.class.getMethod("isChecked"),
						CompoundButton.class.getMethod("setChecked", boolean.class),
						prop, OnCheckedChangeListenerMulticast.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
