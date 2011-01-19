package com.gueei.android.binding;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gueei.android.binding.bindingProviders.AdapterViewProvider;
import com.gueei.android.binding.bindingProviders.CompoundButtonProvider;
import com.gueei.android.binding.bindingProviders.ImageViewProvider;
import com.gueei.android.binding.bindingProviders.TextViewProvider;
import com.gueei.android.binding.bindingProviders.ViewProvider;
import com.gueei.android.binding.exception.AttributeNotDefinedException;
import com.gueei.android.binding.listeners.MulticastListener;

public class Binder {
	private static Application mApplication;
	private ViewFactory viewFactory;
	
	/**
	 * Get the required attribute for the supplied view. This is done internally using the "tag" of the view
	 * so the attribute id must be using the Android id resource type
	 * @param view
	 * @param attributeId must be attribute defined in id type Resource
	 * @return
	 * @throws AttributeNotDefinedException
	 */
	public static ViewAttribute<?, ?> getAttributeForView(View view, String attributeId)
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
		
		ViewAttribute<?, ?> viewAttribute = 
			AttributeBinder.getInstance().createAttributeForView(view, attributeId);
		if (viewAttribute == null) 
			throw new AttributeNotDefinedException
				("The view does not have attribute (id: " + attributeId + ") defined.");
		
		collection.putAttribute(attributeId, viewAttribute);
		return viewAttribute;
	}
		
 	static void putBindingMapToView(View view, BindingMap map){
		view.setTag(R.id.tag_bindingmap, map);
	}
	
	public static BindingMap getBindingMapForView(View view){
		Object map = view.getTag(R.id.tag_bindingmap);
		if(map instanceof BindingMap) return (BindingMap)map;
		return null;
	}
	
	public static void setAndBindContentView(Activity context, int layoutId, Object model){
		InflateResult result = inflateView(context, layoutId, null, false);
		for(View v: result.processedViews){
			AttributeBinder.getInstance().bindView(v, model);
		}
		context.setContentView(result.rootView);
	}
	
	public static InflateResult inflateView(Context context, int layoutId, ViewGroup parent, boolean attachToRoot){
		LayoutInflater inflater = LayoutInflater.from(context).cloneInContext(context);
		ViewFactory factory = new ViewFactory(inflater);
		inflater.setFactory(factory);
		InflateResult result = new InflateResult();
		result.rootView = inflater.inflate(layoutId, parent, attachToRoot);
		result.processedViews = factory.getProcessedViews();
		return result;
	}
	
	public static void init(Application application){
		AttributeBinder.getInstance().registerProvider(new ViewProvider());
		AttributeBinder.getInstance().registerProvider(new TextViewProvider());
		AttributeBinder.getInstance().registerProvider(new AdapterViewProvider());
		AttributeBinder.getInstance().registerProvider(new ImageViewProvider());
		AttributeBinder.getInstance().registerProvider(new CompoundButtonProvider());
		mApplication = application;
	}

	public static Application getApplication(){
		return mApplication;
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
	
	public static class InflateResult{
		public ArrayList<View> processedViews = new ArrayList<View>();
		public View rootView;
	}
}
