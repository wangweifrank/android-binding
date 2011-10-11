package gueei.binding.cursor;

import android.database.Cursor;
import gueei.binding.BindingLog;
import gueei.binding.utility.CacheHashMap;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * User: =ra=
 * Date: 08.10.11
 * Time: 12:12
 */
@SuppressWarnings({"NullableProblems"})
public class RowModelFactory implements IRowModelFactory {
	public static final int DEFAULT_ROW_CACHE_SIZE = 50;

	public <T extends IRowModel> RowModelFactory(Class<T> rowModelType, Cursor cursor, int rowCacheSize) {
		mRowModelType = rowModelType;
		mDataSource = cursor;
		mCachedRowModels = new CacheHashMap<Integer, IRowModel>(rowCacheSize);
		initCursorFields();
	}

	public <T extends IRowModel> RowModelFactory(Class<T> rowModelType, Cursor cursor) {
		this(rowModelType, cursor, DEFAULT_ROW_CACHE_SIZE);
	}

	public <T extends IRowModel> RowModelFactory(Class<T> rowModelType) {
		this(rowModelType, null, DEFAULT_ROW_CACHE_SIZE);
	}

	public <T extends IRowModel> RowModelFactory(Class<T> rowModelType, int rowCacheSize) {
		this(rowModelType, null, rowCacheSize);
	}

	public RowModelFactory() {
		this(null, null, DEFAULT_ROW_CACHE_SIZE);
	}

	@Override
	public void requery() {
		mCachedRowModels.clear();
		if (null != mDataSource) {
			mDataSource.requery();
		}
	}

	@Override
	public void setCursor(Cursor cursor) {
		mCachedRowModels.clear();
		mDataSource = cursor;
	}

	@Override
	public Cursor getCursor() {
		return mDataSource;
	}

	@Override
	public <T extends IRowModel> void setModelType(Class<T> rowModelType) {
		mCachedRowModels.clear();
		mRowModelType = rowModelType;
		initCursorFields();
	}

	@Override
	public Class getModelType() {
		return mRowModelType;
	}

	@Override
	public boolean isModelCached(int position) {
		return mCachedRowModels.containsKey(position);
	}

	@Override
	public int size() {
		return (null == mDataSource) ? 0 : mDataSource.getCount();
	}

	@Override @SuppressWarnings({"unchecked"})
	public <T extends IRowModel> T get(int position) {
		if (null == mDataSource) {
			return null;
		}
		if (mCachedRowModels.containsKey(position)) {
			return (T) mCachedRowModels.get(position);
		}
		else {
			mDataSource.moveToPosition(position);
			T model = readModel();
			mCachedRowModels.put(position, model);
			return model;
		}
	}

	protected <T extends IRowModel> T readModel() {
		T model;
		try {
			//noinspection unchecked
			model = (T) mRowModelType.newInstance();  // create
		}
		catch (Exception e) {
			BindingLog.exception("RowModelFactory: createRowModel", e);
			return null;
		}
		for (Field f : mCursorFields) {   // fill with data from cursor source
			try {
				((CursorField<?>) f.get(model)).fillValue(mDataSource);
			}
			catch (Exception ignored) {
			}
		}
		model.onInitializeFromDS();	// post create task
		return model;
	}

	protected void initCursorFields() {
		mCursorFields.clear();
		if (null != mRowModelType) {
			for (Field f : mRowModelType.getFields()) {
				if (!CursorField.class.isAssignableFrom(f.getType())) {
					continue;
				}
				mCursorFields.add(f);
			}
		}
	}

	protected Class<? extends IRowModel> mRowModelType;
	protected Cursor                     mDataSource;
	protected final ArrayList<Field> mCursorFields = new ArrayList<Field>();
	protected final CacheHashMap<Integer, IRowModel> mCachedRowModels;
}
