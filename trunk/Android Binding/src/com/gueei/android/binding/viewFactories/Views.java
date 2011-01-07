package com.gueei.android.binding.viewFactories;

import java.lang.reflect.Field;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gueei.android.binding.BindedView;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.converters.AttributePropertyBridge;
import com.gueei.android.binding.converters.CharSequenceBridge;
import com.gueei.android.binding.listeners.MulticastListener;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.listeners.OnKeyListenerMulticast;
import com.gueei.android.binding.listeners.TextWatcherMulticast;
import com.gueei.android.binding.viewFactories.ViewFactory.AttributeMap;

public class Views extends BindingViewFactory {

	public View CreateView(String name, Context context,
			AttributeSet attrs) {
		try {
			String prefix = "android.widget.";
			if ((name=="View") || (name=="ViewGroup"))
				prefix = "android.view.";
			if (name.contains("."))
				prefix = null;
			
			View view = LayoutInflater.from(context).createView(name, prefix,
					attrs);
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Binder", e.getMessage());
			return null;
		}
	}

	public void Init() {
	}

	@SuppressWarnings("unchecked")
	public boolean BindView(BindedView view, Binder binder, ViewFactory.AttributeMap attrs,
			Object model) {
		try {
				// Bind enabled
				if (attrs.containsKey("enabled")){
					Field f = model.getClass().getField(attrs.get("enabled"));
					Observable<Boolean> prop = (Observable<Boolean>) f.get(model);
					view.PutConverter("enabled", 
						new AttributePropertyBridge
							((ViewAttribute<Boolean>)view.getAttribute("enabled"), prop));
					prop.notifyChanged();
				}
				if (attrs.containsKey("click")){
					// Bind onClick
					Field command = model.getClass().getDeclaredField(attrs.get("click"));
					Command c = (Command) command.get(model);
					binder.bindCommand(view.getView(), OnClickListenerMulticast.class, c);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void CreateViewAttributes(BindedView view, Binder binder) {
		if (!(view.getView() instanceof View)) return;
		try{
			ViewAttribute<Boolean> attr = view.addAttribute("enabled", 
					View.class.getMethod("isEnabled"),
					View.class.getMethod("setEnabled", boolean.class),
					boolean.class);
		}
		catch(Exception e){}
	}
}
