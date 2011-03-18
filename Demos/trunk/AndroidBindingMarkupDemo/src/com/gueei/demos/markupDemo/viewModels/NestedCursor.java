package com.gueei.demos.markupDemo.viewModels;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.gueei.android.binding.collections.ArrayListObservable;
import com.gueei.android.binding.collections.LazyLoadParent;
import com.gueei.android.binding.cursor.CursorRowModel;
import com.gueei.android.binding.cursor.CursorRowModel.Factory;
import com.gueei.android.binding.cursor.CursorSource;
import com.gueei.android.binding.cursor.IdField;
import com.gueei.android.binding.cursor.StringField;

public class NestedCursor {
	public static class ContactRowModel extends CursorRowModel implements LazyLoadParent{
		public StringField Title = new StringField(1);
		public IdField Id = new IdField(0);
		public ArrayListObservable<String> Emails = 
			new ArrayListObservable<String>(String.class);
		
		@Override
		public void onLoad(int position) {
		}

		public void onLoadChildren() {
			Emails.add("A@BC.com");
			Emails.add("D@EF.com");
		}
		
		@Override
		public String toString(){
			return Title.get();
		}
	}
	
	private static final Factory<ContactRowModel> factory = new Factory<ContactRowModel>(){
		public ContactRowModel createRowModel(Context context) {
			return new ContactRowModel();
		}
	};
	
	public CursorSource<ContactRowModel> Contacts = new 
		CursorSource<ContactRowModel>(ContactRowModel.class, factory);
	
	public NestedCursor(Activity activity){
		Cursor contact = activity.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, 
				new String[]{
						ContactsContract.Contacts._ID,
						ContactsContract.Contacts.DISPLAY_NAME
				}, null, null, null);
		activity.startManagingCursor(contact);
		Contacts.setCursor(contact);
	}
}
