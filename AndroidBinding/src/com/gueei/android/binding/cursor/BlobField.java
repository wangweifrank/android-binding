package com.gueei.android.binding.cursor;

import android.database.Cursor;

public class BlobField extends CursorField<byte[]> {
	public BlobField(int columnIndex) {
		super(columnIndex);
	}

	@Override
	public void fillValue(Cursor cursor) {
		cursor.getBlob(mColumnIndex);
	}

	@Override
	public void saveValue(Cursor cursor) {
	}
}
