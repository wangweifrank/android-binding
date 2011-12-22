package gueei.binding.collections;

import gueei.binding.IObservableCollection;
import gueei.binding.cursor.CursorObservable;
import gueei.binding.cursor.CursorObservableAdapter;
import gueei.binding.viewAttributes.templates.Layout;
import android.content.Context;
import android.widget.Adapter;
import android.widget.Filter;


public class Utility {
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public static Adapter getSimpleAdapter(
			Context context, Object collection, 
			Layout layout, Layout dropDownLayout, Filter filter, String enableItemStatement) throws Exception{
		if ((collection instanceof IObservableCollection)){
			IObservableCollection obsCollection = (IObservableCollection)collection;
			return new CollectionAdapter(
					context, 
					obsCollection, 
					layout, 
					dropDownLayout,
					filter, enableItemStatement);
		}
		if (collection instanceof CursorObservable){
			CursorObservable cobs = (CursorObservable)collection;
			return new CursorObservableAdapter(context, 
					cobs, layout, dropDownLayout);
		}
		/*
		if (collection instanceof CursorRowTypeMap){
			CursorRowTypeMap cursor = (CursorRowTypeMap)collection;
			return new CursorSourceAdapter(BinderV30
					.getApplication(), cursor, layout, dropDownLayout);
		}
		if (collection.getClass().isArray()){
			return new ArrayAdapter(BinderV30.getApplication(),
					collection.getClass().getComponentType(),
					(Object[]) collection, layout, dropDownLayoutId);
		}
		*/
		return null;
	}
	
	public static Adapter getSimpleAdapter(
			Context context, Object collection, 
			Layout layout, Layout dropDownLayout, Filter filter) throws Exception{
		return getSimpleAdapter(context, collection, layout, dropDownLayout, filter, null);
	}
}
