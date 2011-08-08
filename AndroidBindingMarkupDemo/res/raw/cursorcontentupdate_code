package com.gueei.demos.markupDemo.viewModels;
/**
 * User: =ra=
 * Date: 20.07.11
 * Time: 20:36
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import gueei.binding.Command;
import gueei.binding.collections.LazyLoadParent;
import gueei.binding.cursor.*;

@SuppressWarnings({"UnusedDeclaration"})
public class CursorContentUpdate {

	public CursorObservableCollection<GroupsRowModel> Groups;


	public CursorContentUpdate(Activity activity) {
		mContext = activity;
		Groups = new CursorObservableCollection<GroupsRowModel>(activity, GroupsRowModel.class);
		Uri trackingUri = Uri.parse("content://com.gueei.demos/masters");
		Cursor groups = mContext.getContentResolver()
								.query(trackingUri, new String[]{"_ID", "Name", "detailsCount"}, null, null, null);
		activity.startManagingCursor(groups);
		Groups.setCursor(groups);
		Groups.setContentObserverTrackingUri(trackingUri);
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
	private final Context mContext;

	public static class GroupsRowModel extends CursorRowModel implements LazyLoadParent {
		public StringField  Name          = new StringField("Name");
		public IdField      Id            = new IdField("_ID");
		public IntegerField SubItemsCount = new IntegerField("detailsCount");
			public CursorObservableCollection<SubItemRowModel> SubItems;


		@Override public long getId(final long defaultId) {
			return Id.get();
		}

		public void onLoadChildren() {
			SubItems = new CursorObservableCollection<SubItemRowModel>(getContext(), SubItemRowModel.class);
			Uri trackingUri = Uri.parse("content://com.gueei.demos/details");
			Cursor subItems = getContext().getContentResolver()
					.query(trackingUri, new String[]{"_ID", "Name", "masterID"}, "detail.masterID=?",
						   new String[]{Id.get().toString()}, null);
			((Activity) getContext()).startManagingCursor(subItems);
			SubItems.setCursor(subItems);
			SubItems.setContentObserverTrackingUri(trackingUri);
		}
	}

	public static class SubItemRowModel extends CursorRowModel {

		public IdField     Id    = new IdField("_ID");
		public StringField Name  = new StringField("Name");
		public LongField   Group = new LongField("MasterID");

		@Override public long getId(final long defaultId) {
			return Id.get();
		}
	}
}
