package com.gueei.android.binding;

import java.util.HashMap;

public class AttributeCollection {
	
	private HashMap<Integer, ViewAttribute<?,?>> collection;
	
	public AttributeCollection(){
		collection = new HashMap<Integer, ViewAttribute<?,?>>(5);
	}
	
	public boolean containsAttribute(int attrId){
		return collection.containsKey(attrId);
	}
	
	public void putAttribute(int attrId, ViewAttribute<?, ?> attribute){
		collection.put(attrId, attribute);
	}
	
	public ViewAttribute<?, ?> getAttribute(int attrId){
		if (collection.containsKey(attrId)){
			return collection.get(attrId);
		}
		return null;
	}
}
