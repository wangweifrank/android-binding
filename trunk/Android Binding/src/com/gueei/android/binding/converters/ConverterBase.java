package com.gueei.android.binding.converters;

import java.util.AbstractCollection;

import com.gueei.android.binding.Observable;
import com.gueei.android.binding.Observer;
import com.gueei.android.binding.ViewAttribute;

public abstract class ConverterBase<Tv, To> implements Observer{
	protected ViewAttribute<Tv> attribute;
	protected Observable<To> property;
	
	public ConverterBase(ViewAttribute<Tv> attribute, Observable<To> property){
		this.attribute = attribute;
		this.property = property;
		this.attribute.subscribe(this);
		this.property.subscribe(this);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void onPropertyChanged(Observable<T> prop, T newValue,
			AbstractCollection<Object> initiators) {
		if (initiators.contains(this)) return;
		try{
			if (prop.equals(this.attribute)){
				initiators.add(this);
				this.property.set(ConvertTo((Tv)newValue), initiators);
			}
			else if (prop.equals(this.property)){
				initiators.add(this);
				this.attribute.set(ReverseConvert((To)newValue), initiators);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public abstract To ConvertTo(Tv value);
	public abstract Tv ReverseConvert(To value);
}
