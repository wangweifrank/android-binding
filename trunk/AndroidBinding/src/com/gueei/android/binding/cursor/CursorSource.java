package com.gueei.android.binding.cursor;

import java.util.AbstractCollection;

import android.database.Cursor;

import com.gueei.android.binding.Observable;

@SuppressWarnings("unchecked")
public final class CursorSource extends Observable<CursorRowTypeMap> {
	private final CursorRowTypeMap mRowModelType;
	
	public void setCursor(Cursor cursor){
		mRowModelType.setCursor(cursor);
		notifyChanged();
	}

	public CursorSource(Class<?> rowModelType, Object... injectParameters){
		super(CursorRowTypeMap.class);
		mRowModelType = new CursorRowTypeMap(rowModelType, injectParameters);
	}
	
	public Class<? extends CursorRowModel> getRowModelType(){
		return mRowModelType.getRowType();
	}

	@Override
	protected void doSetValue(CursorRowTypeMap newValue,
			AbstractCollection<Object> initiators) {
		// No set 
	}

	@Override
	public CursorRowTypeMap get() {
		return mRowModelType;
	}
}
