package gueei.binding.collections;

import android.database.Cursor;
import android.database.DataSetObserver;
import gueei.binding.cursor.CursorField;
import gueei.binding.cursor.IRowModel;
import gueei.binding.cursor.IRowModelFactory;
import gueei.binding.cursor.RowModelFactory;
import gueei.binding.utility.CacheHashMap;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * User: =ra=
 * Date: 08.10.11
 * Time: 11:58
 */
public class CursorCollection<T extends IRowModel> extends ObservableCollection implements LazyLoadCollection {
	//
	public static interface ICollectionCache<ElType> {
		public static final int DEFAULT_SIZE = 50;
		public void clear(); // remove all items from cache
		public void put(int key, ElType value);
		public ElType get(int key);
		public int size();
		public void reSize(int newSize);
	}

	public CursorCollection(Class<T> rowModelType) {
		this(rowModelType, new RowModelFactory(rowModelType), null);
	}

	public CursorCollection(Class<T> rowModelType, Cursor cursor) {
		this(rowModelType, new RowModelFactory(rowModelType), cursor);
	}

	public CursorCollection(Class<T> rowModelType, IRowModelFactory factory) {
		this(rowModelType, factory, null);
	}

	public CursorCollection(Class<T> rowModelType, IRowModelFactory factory, Cursor cursor) {
		mRowModelType = rowModelType;
		mFactory = factory;
		mCursor = cursor;
		mCollectionCache = new DefaultCache();
		initFieldDataFromModel();
		if (null != cursor) {
			cursor.registerDataSetObserver(mCursorDataSetObserver);
			mCursorDataSetObserver.onChanged();
		}

	}

	public void setCursor(Cursor cursor) {
		if (mCursor == cursor) {
			// cursor is the same, nothing to do
			return;
		}
		if (null != mCursor) {
			// unregister previous cursor listener
			mCursor.unregisterDataSetObserver(mCursorDataSetObserver);
		}
		mCursor = cursor;
		if (null != mCursor) {
			// register listener to new cursor
			mCursor.registerDataSetObserver(mCursorDataSetObserver);
		}
		mCursorDataSetObserver.onChanged(); // imitate changes
	}

	public Cursor getCursor() {
		return mCursor;
	}

	public T getItem(int position) {
		// Check the cache first
		T row = mCollectionCache.get(position);
		if (null == row) { // no such position row cached
			mCursor.moveToPosition(position);
			row = createRowModel();
			mCollectionCache.put(position, row);
		}
		return row;
	}

	public Class<T> getComponentType() {
		return mRowModelType;
	}

	public int size() {
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

	protected void requery() {
		// to be sure data is correct
		if (null != mCursor) {
			mCursor.requery(); // fires mCursorDataSetObserver.onChanged()
		}
		else {
			mCursorDataSetObserver.onChanged();// fire manually
		}
	}

	protected void reInitCacheCursorRowCount() {
		mCollectionCache.clear();
		mCursorRowsCount = (null == mCursor) ? 0 : mCursor.getCount();
	}

	protected void initFieldDataFromModel() {
		for (Field f : mRowModelType.getFields()) {
			if (!CursorField.class.isAssignableFrom(f.getType())) {
				continue;
			}
			mCursorFields.add(f);
		}
	}

	protected T createRowModel() {
		T rowModel = mFactory.createInstance();
		for (Field f : mCursorFields) {
			try {
				((CursorField<?>) f.get(rowModel)).fillValue(mCursor);
			} catch (Exception ignored) {
			}
		}
		rowModel.onInitialize();
		return rowModel;
	}

	@Override
	public void onDisplay(int position) {
		getItem(position).onDisplay();
	}

	@Override
	public void onHide(int position) {
		T row = mCollectionCache.get(position);
		if (null != row) {
			row.onHide();
		}
	}

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
		} catch (Exception ignored) {
		} finally {
			super.finalize();
		}
	}

	protected final Class<T>         mRowModelType;
	protected final IRowModelFactory mFactory;
	protected final ArrayList<Field> mCursorFields = new ArrayList<Field>();
	protected int                 mCursorRowsCount;
	protected Cursor              mCursor;
	// Hold the cached row models
	protected ICollectionCache<T> mCollectionCache;
	protected final DataSetObserver mCursorDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			reInitCacheCursorRowCount();
			notifyCollectionChanged();
		}
	};

	private class DefaultCache implements ICollectionCache<T> {
		private CacheHashMap<Integer, T> mCache;

		public DefaultCache() {
			mCache = new CacheHashMap<Integer, T>(DEFAULT_SIZE);
		}

		public DefaultCache(int cacheSize) {
			mCache = new CacheHashMap<Integer, T>(cacheSize);
		}

		@Override
		public void clear() {
			mCache.clear();
		}

		@Override
		public void put(int key, T value) {
			mCache.put((Integer) key, value);
		}

		@Override
		public T get(int key) {
			return mCache.get((Integer) key);
		}

		@Override
		public int size() {
			return mCache.size();
		}

		@Override
		public void reSize(int newSize) {
			mCache.reSize(newSize);
		}
	}
}