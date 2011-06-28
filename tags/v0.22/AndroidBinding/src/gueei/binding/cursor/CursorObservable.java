package gueei.binding.cursor;

import gueei.binding.BindingLog;
import gueei.binding.Observable;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;


@SuppressWarnings("rawtypes")
public class CursorObservable<T extends CursorRowModel> extends Observable<CursorObservable> {
	private Cursor mCursor;
	private final Class<T> mRowModelType;
	private final CursorRowModel.Factory<T> mFactory;
	private final ArrayList<Field> mCursorFields = new ArrayList<Field>();
	
	public CursorObservable(Class<T> rowModelType) {
		this(rowModelType, new DefaultFactory<T>(rowModelType));
	}
	
	public CursorObservable(Class<T> rowModelType, CursorRowModel.Factory<T> factory){
		super(CursorObservable.class);
		mRowModelType = rowModelType;
		mFactory = factory;
		init();
	}
	
	private void init() {
		for (Field f: mRowModelType.getFields()){
			if (!CursorField.class.isAssignableFrom(f.getType())) continue;
			mCursorFields.add(f);
		}
	}

	public void setCursor(Cursor c){
		mCursor = c;
		this.notifyChanged();
	}
	
	@Override
	public CursorObservable<T> get() {
		return this;
	}

	public Cursor getCursor(){
		return mCursor;
	}
	
	public T newRowModel(Context context){
		T row = mFactory.createRowModel(context);
		row.setCursor(mCursor);
		row.setContext(context);
		return row;
	}
	
	public void fillData(T rowModel, Cursor cursor){
		for(Field f: mCursorFields){
			try{
				((CursorField<?>)f.get(rowModel)).fillValue(cursor);
			}catch(Exception e){
				continue;
			}
		}
	}
	
	// TODO: implement id field
	public long getId(int position){
		return position;
	}
	
	private static class DefaultFactory<T extends CursorRowModel> implements CursorRowModel.Factory<T>{
		private final Class<T> mRowModelType;
		public DefaultFactory(Class<T> rowModelType){
			mRowModelType = rowModelType;
		}
		public T createRowModel(Context context) {
			try {
				return mRowModelType.newInstance();
			} catch (Exception e) {
				BindingLog.exception("CursorObservable: Factory", e);
				return null;
			}
		}
	}
}