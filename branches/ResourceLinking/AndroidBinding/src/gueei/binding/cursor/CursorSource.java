package gueei.binding.cursor;

import gueei.binding.Observable;

import java.util.Collection;

import android.database.Cursor;


@Deprecated
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class CursorSource<T extends CursorRowModel> extends Observable<CursorRowTypeMap> {
	private final CursorRowTypeMap<T> mRowModelType;
	
	public void setCursor(Cursor cursor){
		mRowModelType.setCursor(cursor);
		notifyChanged();
	}

	public CursorSource(Class<T> rowModelType, CursorRowModel.Factory<T> factory){
		super(CursorRowTypeMap.class);
		mRowModelType = new CursorRowTypeMap(rowModelType, factory);
	}
	
	public Class<T> getRowModelType(){
		return mRowModelType.getRowType();
	}

	@Override
	protected void doSetValue(CursorRowTypeMap newValue,
			Collection<Object> initiators) {
		// No set 
	}

	@Override
	public CursorRowTypeMap get() {
		return mRowModelType;
	}
}