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

import com.gueei.android.binding.BindedView;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.Observer;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.converters.AttributePropertyBridge;
import com.gueei.android.binding.converters.CharSequenceBridge;
import com.gueei.android.binding.listeners.KeyListenerMulticast;
import com.gueei.android.binding.listeners.MulticastListener;
import com.gueei.android.binding.listeners.OnCheckedChangeListenerMulticast;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.listeners.OnEditorActionListenerMulticast;
import com.gueei.android.binding.listeners.TextWatcherMulticast;

public class TextViews extends BindingViewFactory {

	public View CreateView(String name, Context context,
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
	public boolean BindView(BindedView view, Binder binder, ViewFactory.AttributeMap attrs,
			Object model) {
		try {
			if (!TextView.class.isInstance(view.getView())) return false;
			if (attrs.containsKey("text"))
			{
				Field f = model.getClass().getField(attrs.get("text"));
				Observable<CharSequence> prop = (Observable<CharSequence>) f.get(model);
				view.PutConverter("text", 
					new CharSequenceBridge((ViewAttribute<CharSequence>)view.getAttribute("text"), prop)
				);
				prop.notifyChanged(prop.get());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}
	
	public void CreateViewAttributes(BindedView view, Binder binder){
		if (!TextView.class.isInstance(view.getView())) return;
		try{
			ViewAttribute<CharSequence> attr = view.addAttribute("text", 
					TextView.class.getMethod("getText"),
					TextView.class.getMethod("setText", CharSequence.class),
					CharSequence.class);
			if (view.getView() instanceof EditText){
				binder.bindCommand(view.getView(), TextWatcherMulticast.class, attr);
			}
		}
		catch(Exception e){}
	}
}