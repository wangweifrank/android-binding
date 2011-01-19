package com.gueei.android.binding.cursor;

import android.database.Cursor;

public class StringField extends CursorField<String> {

	public StringField(int columnIndex) {
		super(columnIndex);
	}

	@Override
	public void fillValue(Cursor cursor) {
		this.set(cursor.getString(mColumnIndex));
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

}
