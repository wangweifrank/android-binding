package gueei.binding.collections;

import android.database.Cursor;
import android.database.DataSetObserver;
import gueei.binding.cursor.IRowModel;
import gueei.binding.cursor.IRowModelFactory;
import gueei.binding.cursor.RowModelFactory;

/**
 * User: =ra=
 * Date: 08.10.11
 * Time: 11:58
 */
public class CursorCollection<T extends IRowModel> extends ObservableCollection implements LazyLoadCollection {
	protected IRowModelFactory mDataSource = null;

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
		mDataSource = factory;
		mDataSource.setCursor(cursor);
		if (null != cursor) {
			cursor.registerDataSetObserver(mCursorDataSetObserver);
		}
	}

	public void setCursor(Cursor cursor) {
		Cursor oldCursor = mDataSource.getCursor();
		if (null != oldCursor) {
			oldCursor.unregisterDataSetObserver(mCursorDataSetObserver);
		}
		mDataSource.setCursor(cursor);
		if (null != cursor) {
			cursor.registerDataSetObserver(mCursorDataSetObserver);
		}
		this.notifyCollectionChanged();
	}

	public Cursor getCursor() {
		return mDataSource.getCursor();
	}

	public T getItem(int position) {
		return mDataSource.get(position);
	}

	@SuppressWarnings({"unchecked"})
	public Class<T> getComponentType() {
		return mDataSource.getModelType();
	}

	public int size() {
		// TODO cache size ...
		return mDataSource.size();
	}

	@Override
	public long getItemId(int position) {
		if (position < size()) {
			return mDataSource.get(position).getId(position);
		}
		return position;
	}

	public void onLoad(int position) {
	}

	/**
	 * Really I don't believe someone cache open cursors in content provider and than
	 * notifies them (cursors) if data changes
	 * but, if cursor is a SQLite cursor directly obtained from a db ...
	 */
	protected DataSetObserver mCursorDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			notifyCollectionChanged();
		}
	};

	protected void finalize() throws Throwable {
		try {
			Cursor oldCursor = mDataSource.getCursor();
			if (null != oldCursor) {
				oldCursor.unregisterDataSetObserver(mCursorDataSetObserver);
				if (!oldCursor.isClosed()) {
					oldCursor.close();
				}
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
		if (position < size()) {
			mDataSource.get(position).onDisplay();
		}
	}

	@Override
	public void onHide(int position) {
		if (position < size() && mDataSource.isModelCached(position)) {
			mDataSource.get(position).onHide();
		}
	}
}