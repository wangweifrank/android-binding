package com.gueei.android.binding.converters;

import java.util.AbstractCollection;

import com.gueei.android.binding.Observable;
import com.gueei.android.binding.ViewAttribute;

public class CharSequenceBridge extends ConverterBase<CharSequence, CharSequence> {
	
	public CharSequenceBridge(ViewAttribute<CharSequence> attribute,
			Observable<CharSequence> property) {
		super(attribute, property);
	}

	@Override
	public CharSequence ConvertTo(CharSequence value) {
		return value;
	}

	@Override
	public CharSequence ReverseConvert(CharSequence value) {
		return value;
	}

	public <T> void onPropertyChanged(Observable<T> prop, T newValue,
			AbstractCollection<Object> initiators) {
		if (!(newValue instanceof CharSequence)) return;
		if (initiators.contains(this)) return;
		CharSequence value = (CharSequence)newValue;
		if (prop.equals(attribute)){
			if (compareCharSequence(property.get(), value))return;
			initiators.add(this);
			property.set(cloneCharSequence(value), initiators);
		}
		else if (prop.equals(property)){
			if (compareCharSequence(attribute.get(), value)) return;
			initiators.add(this);
			attribute.set(cloneCharSequence(value), initiators);
		}
	}
	
	private CharSequence cloneCharSequence(CharSequence o){
		return o.subSequence(0, o.length());
	}
	
	private boolean compareCharSequence(CharSequence a, CharSequence b){
		if(a==null||b==null) return false;
		if(a.length()!=b.length()) return false;
		for(int i=0; i<a.length(); i++){
			if (a.charAt(i)!= b.charAt(i)) return false;
		}
		return true;
	}
}
