package gueei.binding;

import gueei.binding.bindingProviders.IBindingProvider;
import gueei.binding.exception.AttributeNotDefinedException;

import java.util.ArrayList;
import java.util.Map.Entry;

import android.content.Context;
import android.view.View;

public abstract class AttributeBinder<T> {
	
	protected abstract IReferenceObservableProvider<T> getReferenceObservableProvider();
	
	ArrayList<IBindingProvider<T>> providers = new ArrayList<IBindingProvider<T>>(10);

	public Attribute<T, ?> createAttribute(T host, String attributeId) {
		for (IBindingProvider<T> p : providers) {
			Attribute<T, ?> a = p.createAttribute(host, attributeId);
			if (a != null)
				return a;
		}
		return null;
	}

	public void registerProvider(IBindingProvider<T> provider) {
		if (!providers.contains(provider))
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
			T host, String viewAttributeName, String statement, Object model) {
				IObservable<?> property;
				
				// Set the reference context to the current binding view
				getReferenceObservableProvider().setHostContext(host);
				
				property = BindingSyntaxResolver
						.constructObservableFromStatement(context, statement, model, getReferenceObservableProvider());
				if (property != null) {
					try {
						ViewAttribute<?, ?> attr = Binder.getAttributeForView(host,
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

	public AttributeBinder() {
		super();
	}

}