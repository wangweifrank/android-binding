package com.gueei.android.binding.cursor;

import android.database.Cursor;

public class LongField extends CursorField<Long> {

	public LongField(int columnIndex) {
		super(columnIndex);
	}

	@Override
	public void fillValue(Cursor cursor) {
		this.set(cursor.getLong(mColumnIndex));
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

}
