package com.gueei.android.binding.cursor;

import android.database.Cursor;

public class BlobField extends CursorField<byte[]> {
	public BlobField(int columnIndex) {
		super(columnIndex);
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

	@Override
	public byte[] returnValue(Cursor cursor) {
		return cursor.getBlob(mColumnIndex);
	}
}
