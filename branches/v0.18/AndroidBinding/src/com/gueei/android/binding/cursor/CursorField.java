package com.gueei.android.binding.cursor;

import android.database.Cursor;

import com.gueei.android.binding.Observable;

public abstract class CursorField<T> extends Observable<T> {
	
	protected int mColumnIndex = -1;
	
	public CursorField(Class<T> type, int columnIndex){
		super(type);
		mColumnIndex = columnIndex;
	}
	
	public abstract T returnValue(Cursor cursor);
	
	public void fillValue(Cursor cursor){
		this.set(returnValue(cursor));
	}
	
	// Save the data back to the database (if applicable)
	public abstract void saveValue(Cursor cursor);
}
