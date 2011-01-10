package com.gueei.android.binding;

import java.util.HashMap;

import com.gueei.android.binding.converters.ConverterBase;

public class AttributeCollection {
	
	private HashMap<Integer, AttributeBindingSet> collection;
	
	public AttributeCollection(){
		collection = new HashMap<Integer, AttributeBindingSet>(5);
	}
	
	public boolean containsAttribute(int attrId){
		return collection.containsKey(attrId);
	}
	
	public void putAttribute(int attrId, ViewAttribute<?> attribute){
		AttributeBindingSet set = new AttributeBindingSet();
		set.attribute = attribute;
		collection.put(attrId, set);
	}
	
	public ViewAttribute<?> getAttribute(int attrId){
		if (collection.containsKey(attrId)){
			return collection.get(attrId).attribute;
		}
		return null;
	}
	
	public void putConverter(int attrId, ConverterBase<?,?> converter){
		if (!collection.containsKey(attrId)) return;
		collection.get(attrId).converter = converter;
	}
	
	private static class AttributeBindingSet{
		public ViewAttribute<?> attribute;
		public ConverterBase<?, ?> converter;
	}
}
