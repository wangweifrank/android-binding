package gueei.binding;

import gueei.binding.bindingProviders.AbsSpinnerViewProvider;
import gueei.binding.bindingProviders.AdapterViewProvider;
import gueei.binding.bindingProviders.CompoundButtonProvider;
import gueei.binding.bindingProviders.ExpandableListViewProvider;
import gueei.binding.bindingProviders.ImageViewProvider;
import gueei.binding.bindingProviders.ListViewProvider;
import gueei.binding.bindingProviders.ProgressBarProvider;
import gueei.binding.bindingProviders.RatingBarProvider;
import gueei.binding.bindingProviders.SeekBarProvider;
import gueei.binding.bindingProviders.TabHostProvider;
import gueei.binding.bindingProviders.TextViewProvider;
import gueei.binding.bindingProviders.ViewAnimatorProvider;
import gueei.binding.bindingProviders.ViewProvider;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.listeners.ViewMulticastListener;
import gueei.binding.listeners.MulticastListenerCollection;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Binder {
	public static final String BINDING_NAMESPACE = "http://www.gueei.com/android-binding/";
	public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
	
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
		
		AttributeCollection collection = getAttributeCollectionOfView(view);
		if (collection.containsAttribute(attributeId))
			return collection.getAttribute(attributeId);
		
		viewAttribute = AttributeBinder.getInstance().createAttributeForView(view, attributeId);
		
		if (viewAttribute == null) 
			throw new AttributeNotDefinedException
				("The view does not have attribute (id: " + attributeId + ") defined.");
		
		collection.putAttribute(attributeId, viewAttribute);
		return viewAttribute;
	}
	
	/**
	 * Get the associated View Tag of a view, if view tag is not existed or the
	 * existing tag is not view tag, a new viewTag will be created and return.
	 * @param view
	 * @return
	 */
	public static ViewTag getViewTag(View view){
		Object tag = view.getTag();
		if (tag instanceof ViewTag){
			return (ViewTag)tag;
		}
		ViewTag vtag = new ViewTag();
		view.setTag(vtag);
		return vtag;
	}
	
	public static AttributeCollection getAttributeCollectionOfView(View view){
		ViewTag vt = getViewTag(view);
		AttributeCollection collection = vt.get(AttributeCollection.class);
		if (collection!=null)
			return collection;
		collection = new AttributeCollection();
		vt.put(AttributeCollection.class, collection);
		return collection;
	}
	
 	static void putBindingMapToView(View view, BindingMap map){
 		getViewTag(view).put(BindingMap.class, map);
	}
	
	public static BindingMap getBindingMapForView(View view){
		return getViewTag(view).get(BindingMap.class);
	}
	
	public static void setAndBindContentView(Activity context, int layoutId, Object model){
		InflateResult result = inflateView(context, layoutId, null, false);
		context.setContentView(result.rootView);
		for(View v: result.processedViews){
			AttributeBinder.getInstance().bindView(context, v, model);
		}
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
			AttributeBinder.getInstance().bindView(context, v, model);
		}
		return inflatedView.rootView;
	}
	
	private static void attachProcessedViewsToRootView(View rootView, ArrayList<View> processedViews){
		//rootView.setTag(R.id.tag_processedViews, processedViews);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<View> getProcessedViewsFromRootView(View rootView){
		//Object objCollection = rootView.getTag(R.id.tag_processedViews);
		//if (objCollection instanceof ArrayList<?>) return (ArrayList<View>)objCollection;
		return null;
	}
	
	public static void init(Application application){
		AttributeBinder.getInstance().registerProvider(new TabHostProvider());
		AttributeBinder.getInstance().registerProvider(new SeekBarProvider());
		AttributeBinder.getInstance().registerProvider(new RatingBarProvider());
		AttributeBinder.getInstance().registerProvider(new ProgressBarProvider());
		AttributeBinder.getInstance().registerProvider(new ViewAnimatorProvider());
		AttributeBinder.getInstance().registerProvider(new CompoundButtonProvider());
		AttributeBinder.getInstance().registerProvider(new ImageViewProvider());
		AttributeBinder.getInstance().registerProvider(new ExpandableListViewProvider());
		AttributeBinder.getInstance().registerProvider(new AbsSpinnerViewProvider());
		AttributeBinder.getInstance().registerProvider(new ListViewProvider());
		AttributeBinder.getInstance().registerProvider(new AdapterViewProvider());
		AttributeBinder.getInstance().registerProvider(new TextViewProvider());
		AttributeBinder.getInstance().registerProvider(new ViewProvider());
		mApplication = application;
	}

	public static Application getApplication(){
		return mApplication;
	}

	public static <T extends ViewMulticastListener<?>> T getMulticastListenerForView(View view, Class<T> listenerType){
		
		MulticastListenerCollection collection = getViewTag(view).get(MulticastListenerCollection.class);
		if (collection==null){
			collection = new MulticastListenerCollection();
			getViewTag(view).put(MulticastListenerCollection.class, collection);
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
			BindingLog.exception("BinderV30", e);
			return null;
		}		
	}
	
	public static class InflateResult{
		public ArrayList<View> processedViews = new ArrayList<View>();
		public View rootView;
	}
	
	public static String currentVersion(){
		return "0.4";
	}
}
