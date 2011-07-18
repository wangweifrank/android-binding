package gueei.binding.collections;

import gueei.binding.Binder;
import gueei.binding.IObservableCollection;
import gueei.binding.cursor.CursorObservable;
import gueei.binding.cursor.CursorObservableAdapter;
import gueei.binding.viewAttributes.templates.Layout;
import android.content.Context;
import android.widget.Adapter;


public class Utility {
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public static Adapter getSimpleAdapter(
			Context context, Object collection, 
			Layout layout, Layout dropDownLayout) throws Exception{
		if ((collection instanceof IObservableCollection)){
			IObservableCollection obsCollection = (IObservableCollection)collection;
			return new CollectionAdapter(
					Binder.getApplication(), 
					obsCollection, 
					layout, 
					dropDownLayout);
		}
		if (collection instanceof CursorObservable){
			CursorObservable cobs = (CursorObservable)collection;
			return new CursorObservableAdapter(Binder.getApplication(), 
					cobs, layout, dropDownLayout);
		}
		/*
		if (collection instanceof CursorRowTypeMap){
			CursorRowTypeMap cursor = (CursorRowTypeMap)collection;
			return new CursorSourceAdapter(Binder
					.getApplication(), cursor, layout, dropDownLayout);
		}
		if (collection.getClass().isArray()){
			return new ArrayAdapter(Binder.getApplication(),
					collection.getClass().getComponentType(),
					(Object[]) collection, layout, dropDownLayoutId);
		}
		*/
		return null;
	}
}
