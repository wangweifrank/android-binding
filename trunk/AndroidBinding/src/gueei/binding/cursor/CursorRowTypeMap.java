package gueei.binding.cursor;

import android.database.Cursor;
@Deprecated
public class CursorRowTypeMap<T extends CursorRowModel> {
	private Cursor mCursor;
	private final Class<T> mRowType;
	private final CursorRowModel.Factory<T> mFactory;

	public CursorRowTypeMap(Class<T> rowType, CursorRowModel.Factory<T> factory){
		mRowType = rowType;
		mFactory = factory;
	}

	public CursorRowModel.Factory<T> getFactory(){
		return mFactory;
	}

	public Cursor getCursor() {
		return mCursor;
	}

	public void setCursor(Cursor cursor) {
		this.mCursor = cursor;
	}

	public Class<T> getRowType() {
		return mRowType;
	}
}
