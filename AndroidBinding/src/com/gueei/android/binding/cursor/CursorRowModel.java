package com.gueei.android.binding.cursor;

import android.content.Context;
import android.database.Cursor;

public abstract class CursorRowModel {
	public interface Factory<T extends CursorRowModel>{
		public T createRowModel(Context context);
	}
	
	private Context context;
	private Cursor cursor;

	public Cursor getCursor() {
		return cursor;
	}

	void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	private Object[] parameters;
	
	public Object[] getParameters() {
		return parameters;
	}

	void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public CursorRowModel(){}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public abstract void resetInternalState(int position);
}
