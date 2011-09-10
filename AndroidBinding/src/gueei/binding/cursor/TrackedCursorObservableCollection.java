package gueei.binding.cursor;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

/**
 * User: =ra=
 * Date: 10.09.11
 * Time: 18:09
 */
@SuppressWarnings({"UnusedDeclaration"})
public class TrackedCursorObservableCollection<T extends CursorRowModel> extends CursorObservableCollection<T> {

	@SuppressWarnings({"UnusedDeclaration"})
	public TrackedCursorObservableCollection(Context context, Class<T> rowModelType) {
		super(context, rowModelType);
	}

	@SuppressWarnings({"UnusedDeclaration"})
	public TrackedCursorObservableCollection(Context context, Class<T> rowModelType, Cursor cursor) {
		super(context, rowModelType, cursor);
	}

	@SuppressWarnings({"UnusedDeclaration"})
	public TrackedCursorObservableCollection(Context context, Class<T> rowModelType, CursorRowModel.Factory<T> factory) {
		super(context, rowModelType, factory);
	}

	@SuppressWarnings({"UnusedDeclaration"})
	public TrackedCursorObservableCollection(Context context, Class<T> rowModelType, CursorRowModel.Factory<T> factory, Cursor cursor) {
		super(context, rowModelType, factory, cursor);
	}

	@Override public void setCursor(Cursor cursor) {
		if (null != mCursor) {
			unregisterContentObserver();
			mCursor.unregisterDataSetObserver(mCursorDataSetObserver);
		}
		mCursor = cursor;
		if (null != mCursor) {
			mCursor.registerDataSetObserver(mCursorDataSetObserver);
		}
		cacheCursorRowCount();
		this.notifyCollectionChanged();
	}

	/**
	 * There are no obvious methods like AddItem(s), RemoveItem(s), etc for Сursor
	 * data could be changed anywhere and anytime out from model
	 * sometimes we need to know about data changes
	 * Not sure if we have to track more than one uri (!!!)
	 *
	 * @param uri : Uri to track for data changes
	 */
	@SuppressWarnings({"UnusedDeclaration"})
	public void setContentObserverTrackingUri(Uri uri) {
		unregisterContentObserver();
		if (null != uri) {
			registerContentObserver(uri);
		}
	}

	private void registerContentObserver(Uri uri) {
		mContext.getContentResolver().registerContentObserver(uri, false, mCursorContentObserver);
	}

	private void unregisterContentObserver() {
		mContext.getContentResolver().unregisterContentObserver(mCursorContentObserver);
	}

	private final ContentObserver mCursorContentObserver = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			requery();
			// notifyCollectionChanged(); // because requery causes DataSetObserver.onChanged() call
		}
	};

	protected void finalize() throws Throwable {
		try {
			unregisterContentObserver();
		}
		catch (Exception ignored) {
		}
		finally {
			super.finalize();
		}
	}
}
