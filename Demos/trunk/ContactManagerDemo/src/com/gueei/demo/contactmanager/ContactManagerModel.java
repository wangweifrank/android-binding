package com.gueei.demo.contactmanager;

import java.io.InputStream;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.collections.ObservableCursor;
import com.gueei.android.binding.cursor.CursorAdapter;
import com.gueei.android.binding.cursor.CursorRowModel;
import com.gueei.android.binding.cursor.IdField;
import com.gueei.android.binding.cursor.StringField;

public class ContactManagerModel {
	private Activity mContext;
	public Observable<Cursor> ContactList = new Observable<Cursor>();
	public Observable<Boolean> ShowInvisible = new Observable<Boolean>(false);
	public Observable<Object> SelectedContact = new Observable<Object>(new Object());
	public Observable<Long> SelectedContactId = new Observable<Long>(0l);

	public Command ViewContact = new Command(){
		public void Invoke(View view, Object... args) {
			Long x = SelectedContactId.get();
			toastContact(x.toString());
		}
	};
	
	public Command PopulateList = new Command(){
		public void Invoke(View view, Object... args) {
			populateContactList();
		}
	};
	public Command AddContact = new Command(){
		public void Invoke(View view, Object... args) {
			launchContactAdder();
		}
	};
	ObservableCursor collection;
	public ContactManagerModel(Activity context){
		mContext = context;
		populateContactList();
		//ContactList.set(new ObservableCollectionAdapter(mContext, R.layout.contact_entry, collection));
	}
	
	private void toastContact(String id){
		try{
			Cursor c = mContext.getContentResolver().query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
					null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
			c.moveToFirst();
			String email = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			(Toast
				.makeText
				(mContext, 
					"first email: " + email,  
					Toast.LENGTH_SHORT))
			.show();
			c.close();
		}catch(Exception e){}
	}
	
	private void populateContactList() {
        // Build adapter with contact entries
        Cursor cursor = getContacts();
        ContactList.set(cursor);
    }
	
	private Cursor getContacts()
    {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
                (ShowInvisible.get() ? "0" : "1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        return mContext.managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

    protected void launchContactAdder() {
        Intent i = new Intent(mContext, ContactAdder.class);
        mContext.startActivity(i);
    }
}
