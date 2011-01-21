package com.gueei.android.binding.cursor;

import java.util.AbstractCollection;

import android.database.Cursor;

import com.gueei.android.binding.Observable;

public class CursorSource extends Observable<CursorRowTypeMap> {
	private final CursorRowTypeMap mRowModelType;
	
	@Override
	protected void doSetValue(CursorRowTypeMap newValue,
			AbstractCollection<Object> initiators) {
		// Not allowed. Set Cursor only
	}
	
	public void setCursor(Cursor cursor){
		mRowModelType.setCursor(cursor);
		notifyChanged();
	}

	@Override
	public CursorRowTypeMap get() {
		return mRowModelType;
	}

	public CursorSource(Class<? extends CursorRowModel> rowModelType, Object... injectParameters){
		mRowModelType = new CursorRowTypeMap(rowModelType, injectParameters);
	}
	
	public Class<? extends CursorRowModel> getRowModelType(){
		return mRowModelType.getRowType();
	}
}
