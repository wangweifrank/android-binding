package com.gueei.android.binding.bindingProviders;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.Utility;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.exception.AttributeNotDefinedException;

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
		Observable<?> enabled = Utility.getObservableForModel(view, fieldName, model);
		try {
			Binder.getAttributeForView(view, viewAttributeName).BindTo(enabled);
			return true;
		} catch (AttributeNotDefinedException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	protected static void bindViewAttribute(View view, BindingMap map, Object model, String attrName) {
		if (map.containsKey(attrName)){
			bindAttributeWithObservable(view, attrName, map.get(attrName), model);
		}
	}

}
