package com.gueei.android.binding.bindingProviders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.ViewAttribute;

/** 
 * Base class for binding providers. Any special types of views should also inherit this 
 * to provide binding syntax parsing and view attributes creation
 * @author andytsui
 *
 */
public abstract class BindingProvider {
	public abstract <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, int attributeId);
	public abstract boolean bindCommand(View view, int attrId, Command command);
	public abstract void mapBindings(View view, Context context, AttributeSet attrs, BindingMap map);
}
