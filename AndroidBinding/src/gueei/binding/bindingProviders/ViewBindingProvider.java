package gueei.binding.bindingProviders;

import gueei.binding.Attribute;
import gueei.binding.ViewAttribute;
import android.view.View;


/** 
 * Base class for binding providers. Any special types of views should also inherit this 
 * to provide binding syntax parsing and view attributes creation
 * @author andytsui
 *
 */
public abstract class ViewBindingProvider implements IBindingProvider<View> {
	@Override
	public <Ta extends View> Attribute<Ta, ?> createAttribute(View view,
			String attributeId) {
		return createAttributeForView(view, attributeId);
	}

	public abstract <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId);

	// A provider is suppose to be stateless, thus, same class name is enough
	@Override
	public boolean equals(Object o) {
		return this.getClass().getName().equals(o.getClass().getName());
	}
}