package com.gueei.android.binding;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.gueei.android.binding.bindingProviders.TextViewProvider;
import com.gueei.android.binding.bindingProviders.ViewProvider;
import com.gueei.android.binding.converters.ConverterBase;
import com.gueei.android.binding.exception.AttributeNotDefinedException;
import com.gueei.android.binding.listeners.MulticastListener;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class Binder {
	private ViewFactory viewFactory;
	
	/**
	 * Get the required attribute for the supplied view. This is done internally using the "tag" of the view
	 * so the attribute id must be using the Android id resource type
	 * @param view
	 * @param attributeId must be attribute defined in id type Resource
	 * @return
	 * @throws AttributeNotDefinedException
	 */
	public static ViewAttribute<?> getAttributeForView(View view, int attributeId)
		throws AttributeNotDefinedException	{
		Object attributes = view.getTag(R.id.tag_attributes);
		AttributeCollection collection;
		if ((attributes!=null) && (attributes instanceof AttributeCollection)){
			collection = (AttributeCollection)attributes;
			if (collection.containsAttribute(attributeId))
				return collection.getAttribute(attributeId);
		}
		else{
			collection = new AttributeCollection();
			view.setTag(R.id.tag_attributes, collection);
		}
		
		ViewAttribute<?> viewAttribute = 
			AttributeBinder.getInstance().createAttributeForView(view, attributeId);
		if (viewAttribute == null) 
			throw new AttributeNotDefinedException
				("The view does not have attribute (id: " + attributeId + ") defined.");
		
		collection.putAttribute(attributeId, viewAttribute);
		return viewAttribute;
	}

	public static void putConverterForViewAttribute(View view, int attributeId, ConverterBase<?,?> converter) 
		throws AttributeNotDefinedException{
		
		Object attributes = view.getTag(R.id.tag_attributes);
		AttributeCollection collection;
		if ((attributes!=null) && (attributes instanceof AttributeCollection)){
			collection = (AttributeCollection)attributes;
			if (collection.containsAttribute(attributeId))
				collection.putConverter(attributeId, converter);
			else throw new AttributeNotDefinedException();
		}
		throw new AttributeNotDefinedException();
	}
		
 	static void putBindingMapToView(View view, BindingMap map){
		view.setTag(R.id.tag_bindingmap, map);
	}
	
	static BindingMap getBindingMapForView(View view){
		Object map = view.getTag(R.id.tag_bindingmap);
		if(map instanceof BindingMap) return (BindingMap)map;
		return null;
	}
	
	public static void setAndBindContentView(Activity context, int layoutId, Object model){
		context.getLayoutInflater().setFactory(new ViewFactory(model));
		context.setContentView(layoutId);
	}
	
	public static void init(){
		AttributeBinder.getInstance().registerProvider(new ViewProvider());
		AttributeBinder.getInstance().registerProvider(new TextViewProvider());
	}

	public static <T extends MulticastListener<?>> T getMulticastListenerForView(View view, Class<T> listenerType){
		Object tag = view.getTag(R.id.tag_multicastListeners);
		HashMap<Class<T>, T> collection;
		if ((tag==null)||(!(tag instanceof HashMap))){
			collection = new HashMap<Class<T>, T>();
			view.setTag(R.id.tag_multicastListeners, collection);
		}
		else{
			collection = (HashMap<Class<T>, T>)tag;
		}
		if (collection.containsKey(listenerType)){
			return collection.get(listenerType);
		}
		try {
			T listener = listenerType.getConstructor().newInstance();
			listener.registerToView(view);
			collection.put(listenerType, listener);
			return listener;
		} catch (Exception e){
			// TODO: put in log
			return null;
		}		
	}
}
