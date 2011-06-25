package com.gueei.android.binding.cursor;

import android.database.Cursor;

public class FloatField extends CursorField<Float> {

	public FloatField(int columnIndex) {
		super(Float.class, columnIndex);
	}

	@Override
	public Float returnValue(Cursor cursor) {
		return cursor.getFloat(mColumnIndex);
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

}
