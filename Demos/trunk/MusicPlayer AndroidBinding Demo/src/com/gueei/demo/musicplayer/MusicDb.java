package com.gueei.demo.musicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MusicDb {

	private Context mContext;
	MusicDbHelper mDbHelper;
	private SQLiteDatabase mDb;

	public MusicDb(Context context){
		mContext = context;
	}
	
	public MusicDb open() throws SQLException {
        mDbHelper = new MusicDbHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
	
    public boolean entryExists(long id){
    	return mDb.query("music", null, "_id="+id, null, null, null, null).getCount()>0;
    }
    
    public boolean updateEntry(long id, String title, float rating, String artist){
		ContentValues value = new ContentValues();
		value.put("title", title);
		value.put("rating", rating);
		value.put("artist", artist);
		return mDb.update("music", value, "_id=" + id, null)>0;
    }
    
	public long createEntry(long id, String title, float rating, String artist){
		ContentValues value = new ContentValues();
		value.put("_id", id);
		value.put("title", title);
		value.put("rating", rating);
		value.put("artist", artist);
		return mDb.insert("music", null, value);
	}
	
	public Cursor fetchAllEntries(){
		return mDb.query("music", new String[]{
				"_id", "title", "rating", "artist"
		}, null, null, null, null, null);
	}
	
	private static class MusicDbHelper extends SQLiteOpenHelper{
		private static final String DATABASE_NAME = "MusicDb";
	    private static final int DATABASE_VERSION = 4;

		 private static final String DATABASE_CREATE =
		        "create table music (_id integer primary key autoincrement, "
		        + "title text not null, rating number not null default 3, artist text);";

		public MusicDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS music");
            onCreate(db);
		}
	}
}
