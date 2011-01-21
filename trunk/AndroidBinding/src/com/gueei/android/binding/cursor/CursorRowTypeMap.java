package com.gueei.android.binding.cursor;

import android.database.Cursor;

public class CursorRowTypeMap<T extends CursorRowModel> {
	private Cursor mCursor;
	private final Class<T> mRowType;
	private final Object[] mInjectParameters;
	
	public CursorRowTypeMap(Class<T> rowType, Object... parameters){
		mRowType = rowType;
		mInjectParameters = parameters;
	}

	public Cursor getCursor() {
		return mCursor;
	}

	public void setCursor(Cursor cursor) {
		this.mCursor = cursor;
	}

	public Class<T> getRowType() {
		return mRowType;
	}
	
	public Object[] getInjectParameters(){
		return mInjectParameters;
	}
}
