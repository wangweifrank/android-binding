package com.gueei.demo.contactmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.cursor.CursorRowModel;
import com.gueei.android.binding.cursor.CursorSource;
import com.gueei.android.binding.cursor.IdField;
import com.gueei.android.binding.cursor.StringField;
import com.gueei.android.binding.observables.BooleanObservable;
import com.gueei.android.binding.observables.LongObservable;

public class ContactManagerModel {
	private Activity mContext;
	
	public CursorSource<ContactRowModel> ContactList = 
		new CursorSource<ContactRowModel>
			(ContactRowModel.class, new Factory());
	
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
    
	public class Factory implements CursorRowModel.Factory<ContactRowModel>{
		public ContactRowModel createRowModel(Context arg0) {
			return new ContactRowModel();
		}			
	}
    
    public class ContactRowModel extends CursorRowModel {
    	public IdField Id = new IdField(0);
    	public StringField Name = new StringField(1);

    	public Command ShowContact = new Command(){
    		public void Invoke(View view, Object... args) {
    			toastContact(Id.get().toString());
    		}
    	};
    	
		@Override
		public void resetInternalState(int arg0) {
		}
    }
}
