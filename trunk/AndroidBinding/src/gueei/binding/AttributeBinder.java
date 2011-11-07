package gueei.binding;

import gueei.binding.bindingProviders.BindingProvider;
import gueei.binding.exception.AttributeNotDefinedException;

import java.util.ArrayList;
import java.util.Map.Entry;

import android.content.Context;
import android.view.View;

public class AttributeBinder {
	private static AttributeBinder _attributeFactory;
	private ArrayList<BindingProvider> providers = new ArrayList<BindingProvider>(10);

	private AttributeBinder() {
	}

	/**
	 * Ensure it is Singleton
	 * 
	 * @return
	 */
	public static AttributeBinder getInstance() {
		if (_attributeFactory == null)
			_attributeFactory = new AttributeBinder();
		return _attributeFactory;
	}

	public ViewAttribute<?, ?> createAttributeForView(View view,
			String attributeId) {
		for (BindingProvider p : providers) {
			ViewAttribute<?, ?> a = p.createAttributeForView(view, attributeId);
			if (a != null)
				return a;
		}
		return null;
	}

	public void registerProvider(BindingProvider provider) {
		providers.add(provider);
	}

	public void bindView(Context context, View view, Object model) {
		BindingMap map = Binder.getBindingMapForView(view);
		
		// TODO redesign this fix in future 
		// Force to initialize filter attribute
		String filterKey = "filter";
		String filterValue = map.get(filterKey);
		if (null != filterValue) {
			BindingLog.debug("bindView", "Attribute filter shoud be bind before initialize itemSource. To be sure that filtering will be worked.");
			bindAttributeWithObservable(context, view, filterKey, filterValue, model);
		}
		
		for(Entry<String, String> entry: map.getMapTable().entrySet()){
			bindAttributeWithObservable(context, view, entry.getKey(), entry.getValue(), model);
		}
	}

	protected final boolean bindAttributeWithObservable(Context context, 
			View view, String viewAttributeName, String statement, Object model) {
		IObservable<?> property;
		
		// Set the reference context to the current binding view
		refViewAttributeProvider.viewContext = view;
		
		property = BindingSyntaxResolver
				.constructObservableFromStatement(context, statement, model, refViewAttributeProvider);
		if (property != null) {
			try {
				ViewAttribute<?, ?> attr = Binder.getAttributeForView(view,
						viewAttributeName);
				BindingType result = attr.BindTo(context, property);
				if (result.equals(BindingType.NoBinding)) {
					BindingLog.warning("Binding Provider", statement
							+ " cannot setup bind with attribute");
				}
				return true;
			} catch (AttributeNotDefinedException e) {
				e.printStackTrace();
				return false;
			}
		} 
		return false;
	}
	
	private RefViewAttributeProvider refViewAttributeProvider = 
			new RefViewAttributeProvider();
	
	private static class RefViewAttributeProvider implements IReferenceObservableProvider{
		public View viewContext;
		
		public IObservable<?> getReferenceObservable(int referenceId,
				String field) {
			if (viewContext==null) return null;
			
			View reference = viewContext.getRootView().findViewById(referenceId);
			if (reference==null) return null;
			try {
				return Binder.getAttributeForView(reference, field);
			} catch (AttributeNotDefinedException e) {
				return null;
			}
		} 
	}
}
