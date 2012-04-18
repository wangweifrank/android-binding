package gueei.binding;

import gueei.binding.bindingProviders.ViewBindingProvider;
import gueei.binding.exception.AttributeNotDefinedException;

import java.util.ArrayList;

import android.view.View;


public class ViewAttributeBinder extends AttributeBinder {
	private static ViewAttributeBinder _attributeFactory;
	ArrayList<ViewBindingProvider> providers = new ArrayList<ViewBindingProvider>(10);

	private ViewAttributeBinder() {
	}

	/**
	 * Ensure it is Singleton
	 * 
	 * @return
	 */
	public static ViewAttributeBinder getInstance() {
		if (_attributeFactory == null)
			_attributeFactory = new ViewAttributeBinder();
		return _attributeFactory;
	}

	RefViewAttributeProvider refViewAttributeProvider = 
			new RefViewAttributeProvider();

	protected static class RefViewAttributeProvider implements IReferenceObservableProvider{
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

	@Override
	protected IReferenceObservableProvider getReferenceObservableProvider() {
		return refViewAttributeProvider;
	}
}
