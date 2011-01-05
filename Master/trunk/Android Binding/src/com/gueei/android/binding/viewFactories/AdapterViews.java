package com.gueei.android.binding.viewFactories;

import java.lang.reflect.Field;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.listeners.MulticastListener;
import com.gueei.android.binding.listeners.OnCheckedChangeListenerMulticast;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.listeners.OnEditorActionListenerMulticast;
import com.gueei.android.binding.listeners.TextWatcherMulticast;

public class AdapterViews implements BindingViewFactory {

	public View CreateView(String name, Binder binder, Context context,
			AttributeSet attrs) {
		return null;
	}

	public void Init() {
	}

	@SuppressWarnings("unchecked")
	public boolean BindView(View view, Binder binder, ViewFactory.AttributeMap attrs,
			Object model) {
		try {
			if (!AdapterView.class.isInstance(view)) return false;
			if (attrs.containsKey("adapter")){
				Field f = model.getClass().getField(attrs.get("adapter"));
				Observable<Adapter> prop = (Observable<Adapter>) f.get(model);
				binder.bind(view, "Adapter",
						AdapterView.class.getMethod("getAdapter"),
						AdapterView.class.getMethod("setAdapter", Adapter.class), 
						prop);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}
}