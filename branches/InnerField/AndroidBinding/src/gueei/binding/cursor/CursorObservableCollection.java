package gueei.binding.cursor;
/**
 * User: =ra=
 * Date: 05.08.11
 * Time: 15:23
 */

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;
import gueei.binding.BindingLog;
import gueei.binding.collections.LazyLoadCollection;
import gueei.binding.collections.ObservableCollection;
import gueei.binding.utility.CacheHashMap;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Recommend to use instead of CursorObservable only after(!) affirmation by Andy Tsui  (=ra=)
 */
/*
 * Started to work on Adding Caching of Row Models
 */
@SuppressWarnings({"UnusedDeclaration"})
public class CursorObservableCollection<T extends CursorRowModel>
	extends ObservableCollection<T>
	implements LazyLoadCollection{

	// Temp, can change if needed
	private static final int cacheSize = 50;

	private final Class<T>                  mRowModelType;
	private final CursorRowModel.Factory<T> mFactory;
	private final ArrayList<Field> mCursorFields = new ArrayList<Field>();
	private         int     mCursorRowsCount;
	protected final Context mContext;
	protected       Cursor  mCursor;

	// Hold the cached row models
	protected CacheHashMap<Integer, T> mCachedRowModels;



	public CursorObservableCollection(Context context, Class<T> rowModelType) {
		//noinspection NullableProblems
		this(context, rowModelType, new DefaultFactory<T>(rowModelType), null);
	}

	public CursorObservableCollection(Context context, Class<T> rowModelType, Cursor cursor) {
		this(context, rowModelType, new DefaultFactory<T>(rowModelType), cursor);
	}

	public CursorObservableCollection(Context context, Class<T> rowModelType, CursorRowModel.Factory<T> factory) {
		//noinspection NullableProblems
		this(context, rowModelType, factory, null);
	}

	public CursorObservableCollection(Context context, Class<T> rowModelType, CursorRowModel.Factory<T> factory, Cursor cursor) {
		mContext = context;
		mRowModelType = rowModelType;
		mFactory = factory;
		mCursor = cursor;
		if (null != mCursor) {
			mCursor.registerDataSetObserver(mCursorDataSetObserver);
		}
		cacheCursorRowCount();
		mCachedRowModels = new CacheHashMap<Integer, T>(cacheSize);
		init();
	}

	public void setCursor(Cursor cursor) {
		if (null != mCursor) {
			mCursor.unregisterDataSetObserver(mCursorDataSetObserver);
		}
		mCursor = cursor;
		if (null != mCursor) {
			mCursor.registerDataSetObserver(mCursorDataSetObserver);
		}
		cacheCursorRowCount();
		mCachedRowModels.clear(); // 2 be sure data is correct
		this.notifyCollectionChanged();
	}

	public Cursor getCursor() {
		return mCursor;
	}

	public T getItem(int position) {
		// Check the cache first
		if (mCachedRowModels.containsKey(position))
			return mCachedRowModels.get(position);

		else{
			mCursor.moveToPosition(position);
			T row = newRowModel(mContext);
			fillData(row, mCursor);
			mCachedRowModels.put(position, row);
			return row;
		}
	}

	protected void requery() {
		if (null != mCursor) {
			mCursor.requery();
			// mCursorRowsCount = mCursor.getCount(); // will be handled by mCursorDataSetObserver
		}
	}

	public Class<T> getComponentType() {
		return mRowModelType;
	}

	public int size() {
		// return (null == mCursor) ? 0 : mCursor.getCount(); // too much requests..., cached
		return mCursorRowsCount;
	}

	@Override
	public long getItemId(int position) {
		if (0 < mCursorRowsCount) {
			return getItem(position).getId(position);
		}
		return position;
	}

	public void onLoad(int position) {
	}

	protected void cacheCursorRowCount() {
		mCursorRowsCount = (null == mCursor) ? 0 : mCursor.getCount();
	}

	private void init() {
		for (Field f : mRowModelType.getFields()) {
			if (!CursorField.class.isAssignableFrom(f.getType())) {
				continue;
			}
			mCursorFields.add(f);
		}
	}

	private T newRowModel(Context context) {
		T row = mFactory.createRowModel(context);
		row.setCursor(mCursor);
		row.setContext(context);
		return row;
	}

	@SuppressWarnings({"ConstantConditions"})
	private void fillData(T rowModel, Cursor cursor) {
		for (Field f : mCursorFields) {
			try {
				((CursorField<?>) f.get(rowModel)).fillValue(cursor);
			}
			catch (Exception ignored) {
			}
		}
	}

	private static class DefaultFactory<T extends CursorRowModel> implements CursorRowModel.Factory<T> {

		private final Class<T> mRowModelType;

		public DefaultFactory(Class<T> rowModelType) {
			mRowModelType = rowModelType;
		}

		public T createRowModel(Context context) {
			try {
				return mRowModelType.newInstance();
			}
			catch (Exception e) {
				BindingLog.exception("CursorObservable: Factory", e);
				return null;
			}
		}
	}

	/**
	 * Really I don't believe someone cache open cursors in content provider and than
	 * notifies them (cursors) if data changes
	 * but, if cursor is a SQLite cursor directly obtained from a db ...
	 */
	protected DataSetObserver mCursorDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			cacheCursorRowCount();
			mCachedRowModels.clear(); // 2 be sure data is correct
			notifyCollectionChanged();
		}
	};

	protected void finalize() throws Throwable {
		try {
			mCursorRowsCount = 0;
			if (null != mCursor) {
				mCursor.unregisterDataSetObserver(mCursorDataSetObserver);
				if (!mCursor.isClosed()) {
					mCursor.close();
				}
				mCursor = null;
			}
		}
		catch (Exception ignored) {
		}
		finally {
			super.finalize();
		}
	}

	@Override
	public void onDisplay(int position) {
		getItem(position).onDisplay();
	}

	@Override
	public void onHide(int position) {
		if (mCachedRowModels.containsKey(position)){
			getItem(position).onHide();
		}
	}
}
