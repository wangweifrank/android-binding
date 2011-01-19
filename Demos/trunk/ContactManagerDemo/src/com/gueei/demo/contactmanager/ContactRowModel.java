package com.gueei.demo.contactmanager;

import java.io.InputStream;
import java.util.HashMap;

import android.content.ContentUris;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.cursor.CursorRowModel;
import com.gueei.android.binding.cursor.IdField;
import com.gueei.android.binding.cursor.StringField;

public class ContactRowModel extends CursorRowModel{
	public static HashMap<Long, BitmapDrawable> ImageCache = new HashMap<Long, BitmapDrawable>();
	
	public ContactRowModel(){}
	public StringField DisplayName2 = new StringField(1);
	
	public IdField Id = new IdField(0);
	public DependentObservable<CharSequence> DisplayName =  new DependentObservable<CharSequence>(DisplayName2){
		@Override
		public CharSequence calculateValue(Object... args) {
			return "Name: " + args[0];
		}
	};
	
	public DependentObservable<Drawable> Photo = new DependentObservable<Drawable>(Id){
		@Override
		public Drawable calculateValue(Object... args) {
			if (args[0] == null) return null;
			if (ImageCache.containsKey((Long)args[0]))
					return ImageCache.get((Long)args[0]);
			Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, (Long)args[0]);
			InputStream input = 
				ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(), uri);
			if (input==null) {
				ImageCache.put((Long)args[0], null);
				return null;
			}
			ImageCache.put((Long)args[0], new BitmapDrawable(BitmapFactory.decodeStream(input)));
			return ImageCache.get((Long)args[0]);
		}
	};
	
	public Observable<Boolean> DisplayDetail = new Observable<Boolean>(true);
	
	public Command ToggleDetail = new Command(){
		public void Invoke(View view, Object... args) {
			DisplayDetail.set(!DisplayDetail.get());
		}
	};
}