package com.gueei.android.binding.cursor;

import android.database.Cursor;

public class StringField extends CursorField<String> {

	public StringField(int columnIndex) {
		super(columnIndex);
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

	@Override
	public String returnValue(Cursor cursor) {
		return cursor.getString(mColumnIndex);
	}

}
