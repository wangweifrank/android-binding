package com.gueei.android.binding.collections;

import java.util.HashMap;

import android.database.Cursor;

import com.gueei.android.binding.ObservableCollection;

public abstract class ObservableCursor implements ObservableCollection<Object> {

	private final Cursor mCursor;
	private final HashMap<String, Integer> fields;
	
	public ObservableCursor(Cursor cursor){
		mCursor = cursor;
		fields = new HashMap<String, Integer>(cursor.getColumnCount());
		String[] columns = cursor.getColumnNames();
		for(int i=0; i<columns.length; i++){
			fields.put(columns[i], cursor.getColumnIndex(columns[i]));
		}
	}
	
	public boolean containsField(String fieldName) {
		// TODO: check also any static converterible present
		return fields.containsKey(fieldName);
	}

	public int count() {
		return mCursor.getCount();
	}

	public Object getField(int position, String fieldName) {
		// TODO: check also any static converterible present
		mCursor.moveToPosition(position);
		return doGetField(fieldName);
	}
	
	protected abstract Object doGetField(String fieldName);
	
	protected int getFieldIndex(String fieldName){
		return fields.get(fieldName);
	}

	public long getId(int position) {
		mCursor.moveToPosition(position);
		return doGetId();
	}
	
	protected abstract long doGetId();
	
	protected Cursor getCursor(){
		return mCursor;
	}

	public Object getRow(int position) {
		// Not supported
		return null;
	}

	public abstract void setField(int position, String fieldName, Object value);
	
	public void setRow(int position, Object value) {
		// Not supported
	}

	public void Destroy(){
		mCursor.close();
	}

	public void finishBatch() {
		// TODO Auto-generated method stub
		
	}

	public void startBatch() {
		// TODO Auto-generated method stub
		
	}
	
}
