package com.gueei.android.binding.converters;

import java.util.AbstractCollection;

import com.gueei.android.binding.Observable;
import com.gueei.android.binding.ViewAttribute;

public class AttributePropertyBridge<T> extends ConverterBase<T, T> {

	public AttributePropertyBridge(ViewAttribute<T> attribute,
			Observable<T> property) {
		super(attribute, property);
	}

	@Override
	public T ConvertTo(T value) {
		return value;
	}

	@Override
	public T ReverseConvert(T value) {
		return value;
	}
}
