package com.gueei.android.binding;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.gueei.android.binding.bindingProviders.BindingProvider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class AttributeBinder {
	private static AttributeBinder _attributeFactory;
	private ArrayList<BindingProvider> providers = new ArrayList<BindingProvider>(10);

	private AttributeBinder(){}
	
	/**
	 * Ensure it is Singleton
	 * @return
	 */
	public static AttributeBinder getInstance(){
		if (_attributeFactory==null)
			_attributeFactory = new AttributeBinder();
		return _attributeFactory;
	}
	
	public ViewAttribute<?, ?> createAttributeForView(View view, int attributeId){
		for(BindingProvider p: providers){
			ViewAttribute<?, ?> a = p.createAttributeForView(view, attributeId);
			if (a!=null) return a;
		}
		return null;
	}
	
	public void registerProvider(BindingProvider provider){
		if (providers.contains(provider)) return;
		providers.add(provider);
	}
	
	public void mapBindings(View view, Context context, AttributeSet attrs, BindingMap map){
		for (BindingProvider p: providers){
			p.mapBindings(view, context, attrs, map);
		}
	}
	
	public void bindView(View view, Object model){
		BindingMap map = Binder.getBindingMapForView(view);
		for(int attrId : map.attributes.keySet()){
			try {
				Field f = model.getClass().getField(map.attributes.get(attrId));
				Object value = f.get(model);
				if (value instanceof Observable<?>){
					ViewAttribute<?, ?> attr = Binder.getAttributeForView(view, attrId);
//					((Observable<?>)value).subscribe(attr);
					attr.BindTo((Observable<?>)value);
					((Observable<?>)value).notifyChanged();
				}
			} catch (Exception e) {
				e.printStackTrace();
				// Bad argument, ignore this
				// TODO: create a bind logger class to log such stuff
				continue;
			}
		}
		for(int cmdId: map.commands.keySet()){
			try{
				Field f = model.getClass().getField(map.commands.get(cmdId));
				Object value = f.get(model);
				if (value instanceof Command){
					for(BindingProvider p: providers){
						if (p.bindCommand(view, cmdId, (Command)value))
							break;
					}
				}
			}catch(Exception e){
				continue;
			}
		}
	}
}
