package gueei.binding;

import gueei.binding.bindingProviders.AdapterViewProvider;
import gueei.binding.bindingProviders.CompoundButtonProvider;
import gueei.binding.bindingProviders.ImageViewProvider;
import gueei.binding.bindingProviders.ListViewProvider;
import gueei.binding.bindingProviders.ProgressBarProvider;
import gueei.binding.bindingProviders.RatingBarProvider;
import gueei.binding.bindingProviders.SeekBarProvider;
import gueei.binding.bindingProviders.TextViewProvider;
import gueei.binding.bindingProviders.ViewAnimatorProvider;
import gueei.binding.bindingProviders.ViewProvider;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.listeners.MulticastListener;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gueei.binding.R;


public class Binder {
	public static final String BindingNamespace = "http://www.gueei.com/android-binding/";
	private static Application mApplication;
	
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
		
		//  Check if it is custom view, if so, try to look for the attribute in custom view first
		ViewAttribute<?, ?> viewAttribute = null;
		
		if (view instanceof IBindableView){
			viewAttribute = ((IBindableView<?>)view).getViewAttribute(attributeId);
			if (viewAttribute != null)
				return viewAttribute;
		}
		
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
		
		viewAttribute = AttributeBinder.getInstance().createAttributeForView(view, attributeId);
		
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
	
	/**
	 * Inflate, and parse the binding information with Android binding
	 * @param context
	 * @param layoutId The xml layout declaration
	 * @param parent Parent view of the group, just pass null in most cases
	 * @param attachToRoot Pass false
	 * @return Inflate Result. 
	 */
	public static InflateResult inflateView(Context context, int layoutId, ViewGroup parent, boolean attachToRoot){
		LayoutInflater inflater = LayoutInflater.from(context).cloneInContext(context);
		ViewFactory factory = new ViewFactory(inflater);
		inflater.setFactory(factory);
		InflateResult result = new InflateResult();
		result.rootView = inflater.inflate(layoutId, parent, attachToRoot);
		result.processedViews = factory.getProcessedViews();
		return result;
	}
	
	/**
	 * Returns the binded root view of the inflated view
	 * @param context
	 * @param inflatedView The inflated result from inflateView
	 * @param model The view model that is going to bind to
	 * @return RootView of the binded view
	 */
	public static View bindView(Context context, InflateResult inflatedView, Object model){
		for(View v: inflatedView.processedViews){
			AttributeBinder.getInstance().bindView(v, model);
		}
		return inflatedView.rootView;
	}
	
	static void attachProcessedViewsToRootView(View rootView, ArrayList<View> processedViews){
		rootView.setTag(R.id.tag_processedViews, processedViews);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<View> getProcessedViewsFromRootView(View rootView){
		Object objCollection = rootView.getTag(R.id.tag_processedViews);
		if (objCollection instanceof ArrayList<?>) return (ArrayList<View>)objCollection;
		return null;
	}
	
	public static void init(Application application){
		AttributeBinder.getInstance().registerProvider(new SeekBarProvider());
		AttributeBinder.getInstance().registerProvider(new RatingBarProvider());
		AttributeBinder.getInstance().registerProvider(new ProgressBarProvider());
		AttributeBinder.getInstance().registerProvider(new ViewAnimatorProvider());
		AttributeBinder.getInstance().registerProvider(new CompoundButtonProvider());
		AttributeBinder.getInstance().registerProvider(new ImageViewProvider());
		AttributeBinder.getInstance().registerProvider(new ListViewProvider());
		AttributeBinder.getInstance().registerProvider(new AdapterViewProvider());
		AttributeBinder.getInstance().registerProvider(new TextViewProvider());
		AttributeBinder.getInstance().registerProvider(new ViewProvider());
		mApplication = application;
	}

	public static Application getApplication(){
		return mApplication;
	}
	
	@SuppressWarnings("unchecked")
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
			BindingLog.exception("Binder", e);
			return null;
		}		
	}
	
	public static class InflateResult{
		public ArrayList<View> processedViews = new ArrayList<View>();
		public View rootView;
	}
	
	public static String currentVersion(){
		return "0.31";
	}
}
