package com.gueei.android.binding.cursor;

import java.util.AbstractCollection;

import com.gueei.android.binding.Observable;

import android.database.Cursor;

public abstract class CursorField<T> extends Observable<T> {
	
	protected int mColumnIndex = -1;
	
	public CursorField(int columnIndex){
		mColumnIndex = columnIndex;
	}
	
	public abstract T returnValue(Cursor cursor);
	
	public void fillValue(Cursor cursor){
		this.set(returnValue(cursor));
	}
	
	// Save the data back to the database (if applicable)
	public abstract void saveValue(Cursor cursor);
}
