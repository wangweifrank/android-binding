package gueei.binding.cursor;

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

	public CursorRowModel(){}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public void onLoad(int position){}
}
