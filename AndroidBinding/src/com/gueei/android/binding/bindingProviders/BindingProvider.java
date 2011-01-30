package com.gueei.android.binding.bindingProviders;

import java.util.ArrayList;

import android.view.View;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingLog;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.BindingType;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.IObservable;
import com.gueei.android.binding.Utility;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.exception.AttributeNotDefinedException;
import com.gueei.android.binding.listeners.MulticastListener;

/** 
 * Base class for binding providers. Any special types of views should also inherit this 
 * to provide binding syntax parsing and view attributes creation
 * @author andytsui
 *
 */
public abstract class BindingProvider {
	public static final String BindingNamespace = "http://www.gueei.com/android-binding/";
	public abstract <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId);
	public abstract void bind(View view, BindingMap map, Object model);
	
	protected static boolean bindAttributeWithObservable
		(View view, String viewAttributeName, String fieldName, Object model){
		IObservable<?> property = Utility.getObservableForModel(fieldName, model);
		if (property!=null){
			try {
				ViewAttribute<?,?> attr = Binder.getAttributeForView(view, viewAttributeName);
				BindingType result = attr.BindTo(property);
				if (result.equals(BindingType.NoBinding)){
					BindingLog.warning("Binding Provider", fieldName + " cannot setup bind with attribute");
				}
				return true;
			} catch (AttributeNotDefinedException e) {
				e.printStackTrace();
				return false;
			}
		}else{
			// Bind just the value
			Object value = Utility.getFieldForModel(fieldName, model);
			try {
				ViewAttribute<?,?> attr = Binder.getAttributeForView(view, viewAttributeName);
				attr._setObject(value, new ArrayList<Object>());
				return true;
			} catch (AttributeNotDefinedException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	protected static void bindViewAttribute(View view, BindingMap map, Object model, String attrName) {
		if (map.containsKey(attrName)){
			bindAttributeWithObservable(view, attrName, map.get(attrName), model);
		}
	}

	protected static void bindCommand(View view, BindingMap map, Object model, 
			String commandName, Class<? extends MulticastListener<?>> multicastType) {
		if (map.containsKey(commandName)){
			Command command = Utility.getCommandForModel(map.get(commandName), model);
			if (command!=null){
				MulticastListener<?> listener = Binder.getMulticastListenerForView(view, multicastType);
				if (listener!=null)
					listener.register(command);
			}
		}
	}

}
