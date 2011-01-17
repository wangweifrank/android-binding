package com.gueei.demo.contactmanager;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Adapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.adapters.ObservableCollection;

public class ContactManagerModel {
	private Activity mContext;
	public Observable<Adapter> ContactList = new Observable<Adapter>();
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
	
	public ContactManagerModel(Activity context){
		mContext = context;
		// populateContactList();
		ContactList.set(new ObservableCollection(mContext, R.layout.contact_entry));
	}
	
	private void toastContact(String id){
		Cursor c = mContext.getContentResolver().query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
				null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
		try{
			c.moveToFirst();
			String email = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			(Toast
				.makeText
				(mContext, 
					"first email: " + email,  
					Toast.LENGTH_SHORT))
			.show();
		}finally{
			c.close();
		}
	}
	
	private void populateContactList() {
        // Build adapter with contact entries
        Cursor cursor = getContacts();
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME
        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter
        	(mContext, R.layout.contact_entry, cursor,fields, new int[] {R.id.contactEntryText});
        if (ContactList==null)
        	ContactList = new Observable<Adapter>(adapter);
        else
        	ContactList.set(adapter);
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
