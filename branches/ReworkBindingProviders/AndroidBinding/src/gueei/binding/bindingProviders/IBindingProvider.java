package gueei.binding.bindingProviders;

import gueei.binding.Attribute;

public interface IBindingProvider<Th> {
	public abstract <Ta extends Th> Attribute<Ta, ?> createAttribute(Th view, String attributeId);
}