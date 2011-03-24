package com.gueei.android.binding;

import java.util.Hashtable;

import android.view.View;

import com.gueei.android.binding.bindingProviders.BindingProvider;

public class AttributeBinder {
	private static AttributeBinder _attributeFactory;
	private Hashtable<Class<?>, BindingProvider> providers = new Hashtable<Class<?>, BindingProvider>(
			10);

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
		for (BindingProvider p : providers.values()) {
			ViewAttribute<?, ?> a = p.createAttributeForView(view, attributeId);
			if (a != null)
				return a;
		}
		return null;
	}

	public void registerProvider(BindingProvider provider) {
		if (providers.containsKey(provider.getClass()))
			return;
		providers.put(provider.getClass(), provider);
	}

	public void bindView(View view, Object model) {
		bindView(view, model, false);
	}
	
	public void bindView(View view, Object model, boolean oneShot){
		BindingMap map = Binder.getBindingMapForView(view);
		for (BindingProvider p : providers.values()) {
			if(oneShot)
				p.bind(view, map, model, false);
			else
				p.bind(view, map, model, false);
		}
	}
}
