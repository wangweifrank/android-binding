package gueei.binding.cursor;

import android.database.Cursor;

/**
 * User: =ra=
 * Date: 11.10.11
 * Time: 21:19
 */
public interface IRowModelFactory {
	public void setCursor(Cursor cursor);
	public Cursor getCursor();
	public <T extends IRowModel> void setModelType(Class<T> rowModelType);
	public Class getModelType();
	public boolean isModelCached(int position);
	public void requery();
	public int size();
	public <T extends IRowModel> T get(int position);
}
