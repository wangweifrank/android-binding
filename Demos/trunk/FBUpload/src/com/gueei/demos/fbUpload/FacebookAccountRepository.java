package com.gueei.demos.fbUpload;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FacebookAccountRepository {
    public static final String TABLE_ACCOUNT = "account";
    public static final String TABLE_ALBUM = "album";
    public static final String ACCOUNT_ID = "_id";
    public static final String ACCOUNT_NAME = "Name";
    public static final String ALBUM_ID = "_id";
    public static final String ALBUM_NAME = "Name";
    public static final String FK_ALBUM_ACCOUNT_ID = "accountId";

	private Context mContext;
	MusicDbHelper mDbHelper;
	private SQLiteDatabase mDb;

	public FacebookAccountRepository(Context context){
		mContext = context;
	}
	
	public FacebookAccountRepository open() throws SQLException {
        mDbHelper = new MusicDbHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
	
    public Cursor getAllAccounts(){
    	return mDb.query(TABLE_ACCOUNT, null, null, null, null, null, null);
    }
    
    public Cursor getAllAlbumsInAccount(String accountId){
    	return mDb.query(TABLE_ALBUM, null, 
    			FK_ALBUM_ACCOUNT_ID + " ='" + accountId + "'"
    			, null, null, null, null);
    }
    
    public boolean addAccount(String id, String name){
    	if (accountExists(id))
    		return false;
    	ContentValues value = new ContentValues();
    	value.put(ACCOUNT_ID, id);
    	value.put(ACCOUNT_NAME, name);
    	return mDb.insert(TABLE_ACCOUNT, null, value) > 0;
    }
    
    public boolean addAlbum(String id, String name, String accountId){
    	if (albumExists(id))
    		return false;
    	ContentValues value = new ContentValues();
    	value.put(ALBUM_ID, id);
    	value.put(ALBUM_NAME, name);
    	value.put(FK_ALBUM_ACCOUNT_ID, accountId);
    	return mDb.insert(TABLE_ALBUM, null, value) >0;
    }
    
    public boolean albumExists(String id){
    	Cursor search = mDb.query(TABLE_ALBUM, new String[]{ALBUM_ID}, ALBUM_ID + " = '" + id + "'", 
    			null, null, null, null);
    	boolean exists = search.getCount() > 0;
    	search.close();
    	return exists;
    }
    
    public boolean accountExists(String id){
    	Cursor search = mDb.query(TABLE_ACCOUNT, new String[]{ACCOUNT_ID}, ACCOUNT_ID + " = '" + id + "'", 
    			null, null, null, null);
    	boolean exists = search.getCount() > 0;
    	search.close();
    	return exists;
    }
	
	private static class MusicDbHelper extends SQLiteOpenHelper{
		private static final String DATABASE_NAME = "FacebookAccountDb";
	    private static final int DATABASE_VERSION = 2;

		public MusicDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String createAccountTable = new CreateTableScriptBuilder()
				.start(TABLE_ACCOUNT)
				.addColumn(ACCOUNT_ID, "text", "primary key")
				.addColumn(ACCOUNT_NAME, "text")
				.end()
				.getScript();
			
			String createAlbumTable = new CreateTableScriptBuilder()
				.start(TABLE_ALBUM)
				.addColumn(ALBUM_ID, "text", "primary key")
				.addColumn(ALBUM_NAME, "text")
				.addColumn(FK_ALBUM_ACCOUNT_ID, "text")
				.foreignKey(FK_ALBUM_ACCOUNT_ID, TABLE_ACCOUNT, ACCOUNT_ID)
				.end()
				.getScript();
			
			db.execSQL(createAccountTable);
			db.execSQL(createAlbumTable);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
            onCreate(db);
		}
	}
	
	private static class CreateTableScriptBuilder{
		private StringBuilder mBuilder;
		private boolean justStart = true;
		public CreateTableScriptBuilder start(String tableName){
			mBuilder = new StringBuilder();
			justStart = true;
			mBuilder.append("create table " + tableName + " ( ");
			return this;
		}
		
		public CreateTableScriptBuilder addColumn(String columnName, String type){
			return addColumn(columnName, type, "");
		}
		
		public CreateTableScriptBuilder addColumn(String columnName, String type, String additionalParam){
			if (!justStart)
				mBuilder.append(", ");
			mBuilder.append(columnName + " " + type + " " + additionalParam);
			justStart = false;
			return this;
		}
		
		public CreateTableScriptBuilder foreignKey(String keyColumnName, String referenceTable, String referenceField){
			if (!justStart)
				mBuilder.append(", ");
			mBuilder.append("foreign key(" + keyColumnName + ") ");
			mBuilder.append("references " + referenceTable + "(" + referenceField + ")");
			justStart= false;
			return this;
		}
		
		public CreateTableScriptBuilder end(){
			mBuilder.append("); ");
			return this;
		}
		
		public String getScript(){
			return mBuilder.toString();
		}
	}
}
