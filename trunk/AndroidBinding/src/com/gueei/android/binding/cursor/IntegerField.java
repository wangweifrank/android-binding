package com.gueei.android.binding.cursor;

import android.database.Cursor;

public class IntegerField extends CursorField<Integer> {

	public IntegerField(int columnIndex) {
		super(columnIndex);
	}

	@Override
	public void fillValue(Cursor cursor) {
		this.set(cursor.getInt(mColumnIndex));
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

}
