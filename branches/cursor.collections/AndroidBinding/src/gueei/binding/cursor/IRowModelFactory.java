package gueei.binding.cursor;

import android.database.Cursor;

/**
 * User: =ra=
 * Date: 11.10.11
 * Time: 21:19
 */
public interface IRowModelFactory {
	public  <T extends IRowModel> T createInstance();
}
