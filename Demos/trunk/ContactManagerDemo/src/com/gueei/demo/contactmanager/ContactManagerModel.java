package com.gueei.demo.contactmanager;

import gueei.binding.Command;
import gueei.binding.collections.CursorCollection;
import gueei.binding.cursor.IRowModelFactory;
import gueei.binding.cursor.IdField;
import gueei.binding.cursor.RowModel;
import gueei.binding.cursor.StringField;
import gueei.binding.observables.BooleanObservable;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

public class ContactManagerModel {
	private Activity mContext;
	
	public CursorCollection<ContactRowModel> ContactList = 
		new CursorCollection<ContactRowModel>
			(ContactRowModel.class, new IRowModelFactory<ContactRowModel>(){
				@Override
				public ContactRowModel createInstance() {
					return new ContactRowModel();
				}
			});
	
	public BooleanObservable ShowInvisible = new BooleanObservable(false);

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
		populateContactList();
	}
	
	private void toastContact(String id){
		Cursor c = null;
		try{
			c = mContext.getContentResolver().query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
					null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
			if (c.moveToFirst()){
				String email = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				(Toast
					.makeText
					(mContext,
						"first email: " + email,  
						Toast.LENGTH_SHORT))
				.show();
			}
		}finally{
			if (c!=null)
				c.close();
		}
	}
	
	private void populateContactList() {
        // Build adapter with contact entries
        Cursor cursor = getContacts();
        ContactList.setCursor(cursor);
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
    
    public class ContactRowModel extends RowModel {
    	public IdField Id = new IdField(0);
    	public StringField Name = new StringField(1);

    	public Command ShowContact = new Command(){
    		public void Invoke(View view, Object... args) {
    			toastContact(Id.get().toString());
    		}
    	};
    }
}
