package com.gueei.android.binding.cursor;

import android.database.Cursor;

public class IntegerField extends CursorField<Integer> {

	public IntegerField(int columnIndex) {
		super(Integer.class, columnIndex);
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

	@Override
	public Integer returnValue(Cursor cursor) {
		return cursor.getInt(mColumnIndex);
	}

}
