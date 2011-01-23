package com.gueei.android.binding;

import java.util.ArrayList;

import android.view.View;

import com.gueei.android.binding.bindingProviders.BindingProvider;

public class AttributeBinder {
	private static AttributeBinder _attributeFactory;
	private ArrayList<BindingProvider> providers = new ArrayList<BindingProvider>(
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
		for (BindingProvider p : providers) {
			ViewAttribute<?, ?> a = p.createAttributeForView(view, attributeId);
			if (a != null)
				return a;
		}
		return null;
	}

	public void registerProvider(BindingProvider provider) {
		if (providers.contains(provider))
			return;
		providers.add(provider);
	}

	public void bindView(View view, Object model) {
		BindingMap map = Binder.getBindingMapForView(view);
		for (BindingProvider p : providers) {
			p.bind(view, map, model);
		}
	}
}
