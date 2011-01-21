package com.gueei.android.binding.cursor;

import android.database.Cursor;

public class LongField extends CursorField<Long> {

	public LongField(int columnIndex) {
		super(columnIndex);
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

	@Override
	public Long returnValue(Cursor cursor) {
		return cursor.getLong(mColumnIndex);
	}

}
