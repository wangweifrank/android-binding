package com.gueei.android.binding.collections;

import android.content.Context;
import android.widget.Adapter;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.IObservableCollection;
import com.gueei.android.binding.cursor.CursorObservable;
import com.gueei.android.binding.cursor.CursorObservableAdapter;
import com.gueei.android.binding.cursor.CursorRowTypeMap;
import com.gueei.android.binding.cursor.CursorSourceAdapter;

public class Utility {
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public static Adapter getSimpleAdapter(
			Context context, Object collection, 
			int layoutId, int dropDownLayoutId) throws Exception{
		if ((collection instanceof IObservableCollection)){
			IObservableCollection obsCollection = (IObservableCollection)collection;
			return new CollectionAdapter(
					Binder.getApplication(), 
					obsCollection, 
					layoutId, 
					dropDownLayoutId);
		}
		if (collection instanceof CursorObservable){
			CursorObservable cobs = (CursorObservable)collection;
			return new CursorObservableAdapter(Binder.getApplication(), 
					cobs, layoutId, dropDownLayoutId);
		}
		if (collection instanceof CursorRowTypeMap){
			CursorRowTypeMap cursor = (CursorRowTypeMap)collection;
			return new CursorSourceAdapter(Binder
					.getApplication(), cursor, layoutId, dropDownLayoutId);
		}
		if (collection.getClass().isArray()){
			return new ArrayAdapter(Binder.getApplication(),
					collection.getClass().getComponentType(),
					(Object[]) collection, layoutId, dropDownLayoutId);
		}
		return null;
	}
}
