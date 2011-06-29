package gueei.binding;

import gueei.binding.bindingProviders.BindingProvider;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.listeners.MulticastListener;

import java.util.ArrayList;
import java.util.Hashtable;

import android.view.View;

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
		BindingMap map = Binder.getBindingMapForView(view);
		if (view instanceof IBindableView){
			for(String attrName : map.getAllKeys()){
				IBindableView<?> bview = (IBindableView<?>)view;
				switch(bview.getAttributeHandlingMethod(attrName)){
				case ViewAttribute:
					if (bindViewAttribute(view, map, model, attrName))
						map.setAsHandled(attrName);
					break;
				case Command:
					break;
				default:
					break;
				}
			}
		}
		for (BindingProvider p : providers.values()) {
			p.bind(view, map, model);
		}
	}

	protected final boolean bindAttributeWithObservable(View view,
			String viewAttributeName, String statement, Object model) {
		IObservable<?> property;
		property = Utility.getObservableForModel(view.getContext(), statement,
				model);
		if (property != null) {
			try {
				ViewAttribute<?, ?> attr = Binder.getAttributeForView(view,
						viewAttributeName);
				BindingType result = attr.BindTo(property);
				if (result.equals(BindingType.NoBinding)) {
					BindingLog.warning("Binding Provider", statement
							+ " cannot setup bind with attribute");
				}
				return true;
			} catch (AttributeNotDefinedException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			// Bind just the value
			Object value = Utility.getFieldForModel(statement, model);
			try {
				ViewAttribute<?, ?> attr = Binder.getAttributeForView(view,
						viewAttributeName);
				attr._setObject(value, new ArrayList<Object>());
				return true;
			} catch (AttributeNotDefinedException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	protected final boolean bindViewAttribute(View view, BindingMap map,
			Object model, String attrName) {
		if (map.containsKey(attrName)) {
			return bindAttributeWithObservable(view, attrName, map.get(attrName),
					model);
		}
		return false;
	}

	protected final void bindCommand(View view, BindingMap map, Object model,
			String commandName,
			Class<? extends MulticastListener<?>> multicastType) {
		if (map.containsKey(commandName)) {
			Command command = Utility.getCommandForModel(map.get(commandName),
					model);
			if (command != null) {
				MulticastListener<?> listener = Binder
						.getMulticastListenerForView(view, multicastType);
				if (listener != null)
					listener.register(command);
			}
		}
	}
}
