package com.gueei.android.binding.viewFactories;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
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
import com.gueei.android.binding.listeners.OnCheckedChangeListenerMulticast;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.listeners.TextWatcherMulticast;

public class CompoundButtons extends BindingViewFactory {

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
				view.PutConverter("checked", 
						new AttributePropertyBridge((ViewAttribute<Boolean>)view.getAttribute("checked"), prop)
					);
					prop.notifyChanged(prop.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public View CreateView(String name, Context context, AttributeSet attrs) {
		return null;
	}

	public void CreateViewAttributes(BindedView view, Binder binder) {
		if (!CompoundButton.class.isInstance(view.getView())) return;
		try{
			ViewAttribute<Boolean> attr = view.addAttribute("checked", 
					CompoundButton.class.getMethod("isChecked"),
					CompoundButton.class.getMethod("setChecked", boolean.class),
					boolean.class);
			binder.bindCommand(view.getView(), OnCheckedChangeListenerMulticast.class, attr);
		}
		catch(Exception e){}
	}
}
