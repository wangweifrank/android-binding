package com.gueei.android.binding.collections;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.gueei.android.binding.ObservableCollection;

public class ObservableArray<T> implements ObservableCollection<T> {
	private final T[] mArray;
	
	public ObservableArray(T[] array){
		mArray = array;
	}
	
	public int count() {
		return mArray.length;
	}

	public Object getField(int position, String fieldName) {
		if (position >= count()) return null;
		
		if (fieldName.equals(".")) return mArray[position];
		
		return null;
	}

	public long getId(int position) {
		return position;
	}

	public T getRow(int position) {
		if (position >= count()) return null;
		return mArray[position];
	}

	public void setField(int position, String fieldName, Object value) {
		if (position >= count()) return;
		
		if (fieldName.equals(".")) mArray[position] = (T)value;
	}

	public void setRow(int position, T value) {
		if (position >= count()) return;
		mArray[position] = value;
	}

	public boolean containsField(String fieldName) {
		if (fieldName.equals(".")) return true;
		return false;
	}

	public void finishBatch() {
		// Do nothing
	}

	public void startBatch() {
		// Do nothing
	}
}
