package gueei.binding.collections;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import gueei.binding.cursor.IRowModel;
import gueei.binding.cursor.IRowModelFactory;
import gueei.binding.cursor.RowModelFactory;

/**
 * User: =ra=
 * Date: 08.10.11
 * Time: 12:05
 */
public class TrackedCursorCollection<T extends IRowModel> extends CursorCollection<T> {
	public TrackedCursorCollection(Class<T> rowModelType) {
		this(rowModelType, new RowModelFactory(rowModelType), null);
	}

	public TrackedCursorCollection(Class<T> rowModelType, Cursor cursor) {
		this(rowModelType, new RowModelFactory(rowModelType), cursor);
	}

	public TrackedCursorCollection(Class<T> rowModelType, IRowModelFactory factory) {
		this(rowModelType, factory, null);
	}

	public TrackedCursorCollection(Class<T> rowModelType, IRowModelFactory factory, Cursor cursor) {
		super(rowModelType, factory, cursor);
	}

	@Override
	public void setCursor(Cursor cursor) {
		Cursor oldCursor = mDataSource.getCursor();
		if (null != oldCursor) {
			mCursorContentObserver.unregisterUri();
			oldCursor.unregisterDataSetObserver(mCursorDataSetObserver);
		}
		mDataSource.setCursor(cursor);
		if (null != cursor) {
			cursor.registerDataSetObserver(mCursorDataSetObserver);
		}
		this.notifyCollectionChanged();
	}

	/**
	 * There are no obvious methods like AddItem(s), RemoveItem(s), etc for Сursor
	 * data could be changed anywhere and anytime out from model
	 * sometimes we need to know about data changes
	 * Not sure if we have to track more than one uri (!!!)
	 *
	 * @param context			  Context to register for data changes
	 * @param uri				  The URI to watch for changes. This can be a specific row URI, or a base URI
	 *                             for a whole class of content.
	 * @param notifyForDescendents If <code>true</code> changes to URIs beginning with <code>uri</code>
	 *                             will also cause notifications to be sent. If <code>false</code> only changes to
	 *                             the exact URI
	 *                             specified by <em>uri</em> will cause notifications to be sent. If true,
	 *                             than any URI values
	 *                             at or below the specified URI will also trigger a match.
	 */
	public void setContentObserverTrackingUri(Context context, Uri uri, boolean notifyForDescendents) {
		mCursorContentObserver.unregisterUri();
		if (null != uri) {
			mCursorContentObserver.registerUri(context, uri, notifyForDescendents);
		}
	}

	protected class CollectionContentObserver extends ContentObserver {
		public CollectionContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			mDataSource.requery();
		}

		public void registerUri(Context context, Uri uri, boolean notifyForDescendents) {
			unregisterUri();
			if (null != (mContext = context)) {
				mContext.getContentResolver().registerContentObserver(uri, notifyForDescendents, this);
			}
		}

		public void unregisterUri() {
			if (null != mContext) {
				mContext.getContentResolver().unregisterContentObserver(this);
			}
		}

		protected Context mContext = null;
	}

	private final CollectionContentObserver mCursorContentObserver = new CollectionContentObserver(new Handler());

	protected void finalize() throws Throwable {
		try {
			mCursorContentObserver.unregisterUri();
		}
		catch (Exception ignored) {
		}
		finally {
			super.finalize();
		}
	}
}
