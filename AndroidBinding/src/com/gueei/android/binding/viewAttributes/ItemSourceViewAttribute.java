package com.gueei.android.binding.viewAttributes;

import java.util.AbstractCollection;

import android.database.Cursor;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingMap;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.Utility;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.cursor.CursorAdapter;
import com.gueei.android.binding.cursor.CursorRowModel;

public class ItemSourceViewAttribute extends ViewAttribute<AdapterView, Cursor> {

	private Cursor mCursor;
	
	public ItemSourceViewAttribute(AdapterView view, String attributeName) {
		super(view, attributeName);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue==null) return;
		if (!(newValue instanceof Cursor)) return;
		
		mCursor = (Cursor)newValue;
		if (mCursor.isClosed()) return;
		
		BindingMap map = Binder.getBindingMapForView(this.view.get());
		if (!map.containsKey("itemTemplate")) return;
		if (!map.containsKey("itemDataType")) return;
		
		int itemTemplate = Utility.resolveResource(map.get("itemTemplate"), Binder.getApplication());
		if (itemTemplate<0) return;

		try {
			// Resolve item data type
			Class<CursorRowModel> itemType = 
				(Class<CursorRowModel>)Class.forName(map.get("itemDataType"), true, Binder.getApplication().getClassLoader());
			CursorAdapter<CursorRowModel> adapter = new CursorAdapter<CursorRowModel>(
					Binder.getApplication(), itemType, mCursor, itemTemplate);
			view.get().setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public Cursor get() {
		return mCursor;
	}
}
