package com.gueei.android.binding.viewFactories;

import java.lang.reflect.Field;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

public class TextViews implements BindingViewFactory {

	public View CreateView(String name, Binder binder, Context context,
			AttributeSet attrs) {
		return null;
	}

	public void Init() {
		MulticastListener.Factory.RegisterConstructor
		(TextView.OnEditorActionListener.class, 
				OnEditorActionListenerMulticast.class);
		MulticastListener.Factory.RegisterConstructor(TextWatcher.class, TextWatcherMulticast.class);
	}

	@SuppressWarnings("unchecked")
	public boolean BindView(View view, Binder binder, ViewFactory.AttributeMap attrs,
			Object model) {
		try {
			if (!TextView.class.isInstance(view)) return false;
			if (attrs.containsKey("text")){
				Field f = model.getClass().getField(attrs.get("text"));
				Observable<?> prop = (Observable<?>) f.get(model);
				if (EditText.class.isInstance(view)){
					binder.bind(view, "Text",
							TextView.class.getMethod("getText"),
							TextView.class.getMethod("setText", CharSequence.class), 
							prop, TextWatcherMulticast.class);
				}
				else{
					binder.bind(view, "Text",
							TextView.class.getMethod("getText"),
							TextView.class.getMethod("setText", CharSequence.class), 
							prop);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}
}