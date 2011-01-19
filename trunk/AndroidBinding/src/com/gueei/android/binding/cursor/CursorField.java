package com.gueei.android.binding.cursor;

import java.util.AbstractCollection;

import com.gueei.android.binding.Observable;

import android.database.Cursor;

public abstract class CursorField<T> extends Observable<T> {
	
	protected int mColumnIndex = -1;
	
	public CursorField(int columnIndex){
		mColumnIndex = columnIndex;
	}
		
	// Must call set() to set the value of itself
	public abstract void fillValue(Cursor cursor);
	
	// Save the data back to the database (if applicable)
	public abstract void saveValue(Cursor cursor);
}
