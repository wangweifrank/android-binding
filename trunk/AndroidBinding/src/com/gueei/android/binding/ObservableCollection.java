package com.gueei.android.binding;

public interface ObservableCollection<T> {
	public int count();
	public Object getField(int position, String fieldName);
	public void setField(int position, String fieldName, Object value);
	public T getRow(int position);
	public void setRow(int position, T value);
	public long getId(int position);
	public boolean containsField(String fieldName);
	public void startBatch();
	public void finishBatch();
}
