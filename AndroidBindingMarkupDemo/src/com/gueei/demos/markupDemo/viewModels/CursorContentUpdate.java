package com.gueei.demos.markupDemo.viewModels;
/**
 * User: =ra=
 * Date: 20.07.11
 * Time: 20:36
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import gueei.binding.Command;
import gueei.binding.collections.LazyLoadParent;
import gueei.binding.cursor.*;

public class CursorContentUpdate {

	private Handler mHandler = new Handler();
	private Cursor  mCursor  = null;

	class MasterContentObserver extends ContentObserver {

		public MasterContentObserver(Handler h) {
			super(h);
		}

		public void onChange(boolean selfChange) {
			if (null != mCursor) {
				mCursor.requery();
			}
		}
	}

	private MasterContentObserver mMasterObserver = null;

	private void registerContentObservers() {
		ContentResolver contentResolver = mContext.getContentResolver();
		mMasterObserver = new MasterContentObserver(mHandler);
		contentResolver.registerContentObserver(Uri.parse("content://com.gueei.demos/masters"), false,
												mMasterObserver);
	}

	private void unregisterContentObservers() {
		ContentResolver contentResolver = mContext.getContentResolver();
		if (mMasterObserver != null) {		// just paranoia
			contentResolver.unregisterContentObserver(mMasterObserver);
			mMasterObserver = null;
		}
	}

	protected void finalize() throws Throwable {
		try {
			if (null != mCursor) {
				mCursor.close();
				mCursor = null;
			}
			unregisterContentObservers();
		}
		catch (Exception e) {
		}
		finally {
			super.finalize();
		}
	}

	private final Context mContext;

	public CursorContentUpdate(Activity activity) {
		mContext = activity;
		Cursor groups = mContext.getContentResolver().query(Uri.parse("content://com.gueei.demos/masters"),
															new String[]{"_ID", "Name", "detailsCount"}, null, null,
															null);
		activity.startManagingCursor(groups);
		Groups.setCursor(groups);
		mCursor = groups;
		registerContentObservers();
	}

	public final Command AddSubItem    = new Command() {
		@Override
		public void Invoke(android.view.View view, Object... args) {
			ContentValues values = new ContentValues();
			values.put("Name", "Child for Group 1");
			values.put("masterID", "1");
			Uri uri = mContext.getContentResolver().insert(Uri.parse("content://com.gueei.demos/details"), values);
		}
	};
	public final Command RemoveSubItem = new Command() {
		@Override
		public void Invoke(android.view.View view, Object... args) {
			mContext.getContentResolver().delete(Uri.parse("content://com.gueei.demos/details"),
												 "detail._ID=(SELECT min(_ID) FROM detail WHERE" + " masterID=1)",
												 null);
		}
	};
	public final Command RestoreData   = new Command() {
		@Override
		public void Invoke(android.view.View view, Object... args) {
			mContext.getContentResolver().insert(Uri.parse("content://com.gueei.demos/restore"), null);
		}
	};

	public static class GroupsRowModel extends CursorRowModel implements LazyLoadParent {

		public StringField                       Name          = new StringField("Name");
		public IdField                           Id            = new IdField("_ID");
		public IntegerField                      SubItemsCount = new IntegerField("detailsCount");
		public CursorObservable<SubItemRowModel> SubItems      =
				new CursorObservable<SubItemRowModel>(SubItemRowModel.class);

		@Override
		public void onLoad(int position) {
		}

		public void onLoadChildren() {
			Cursor c = getContext().getContentResolver()
					.query(Uri.parse("content://com.gueei.demos/details"), new String[]{"_ID", "Name"},
						   "detail.masterID=?", new String[]{Id.get().toString()}, null);
			SubItems.setCursor(c);
		}
	}

	public static class SubItemRowModel extends CursorRowModel {

		public StringField Name  = new StringField("Name");
		public LongField   Group = new LongField("MasterID");
	}

	public CursorObservable<GroupsRowModel> Groups = new CursorObservable<GroupsRowModel>(GroupsRowModel.class);
}
