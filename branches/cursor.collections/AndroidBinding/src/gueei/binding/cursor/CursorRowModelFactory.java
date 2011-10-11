package gueei.binding.cursor;

import android.content.Context;
import android.database.Cursor;
import gueei.binding.BindingLog;

import java.lang.reflect.Field;

/**
 * User: =ra=
 * Date: 11.10.11
 * Time: 23:23
 */
@Deprecated // Backward compatibility
public class CursorRowModelFactory extends RowModelFactory {
	private final Context mContext;

	public <T extends CursorRowModel> CursorRowModelFactory(Class<T> rowModelType, Cursor cursor, int rowCacheSize,
															Context context) {
		super(rowModelType, cursor, rowCacheSize);
		mContext = context;
	}

	public <T extends CursorRowModel> CursorRowModelFactory(Class<T> rowModelType, Cursor cursor, Context context) {
		this(rowModelType, cursor, DEFAULT_ROW_CACHE_SIZE, context);
	}

	public <T extends CursorRowModel> CursorRowModelFactory(Class<T> rowModelType, Context context) {
		this(rowModelType, null, DEFAULT_ROW_CACHE_SIZE, context);
	}

	public <T extends CursorRowModel> CursorRowModelFactory(Class<T> rowModelType, int rowCacheSize,
															Context context) {
		this(rowModelType, null, rowCacheSize, context);
	}

	public CursorRowModelFactory(Context context) {
		this(null, null, DEFAULT_ROW_CACHE_SIZE, context);
	}

	@Override   // Inaccurate, dirty
	protected <T extends IRowModel> T readModel() {
		T model;
		try {
			//noinspection unchecked
			model = (T) mRowModelType.newInstance();  // create
		}
		catch (Exception e) {
			BindingLog.exception("RowModelFactory: createRowModel", e);
			return null;
		}
		((CursorRowModel) model).setContext(mContext);
		for (Field f : mCursorFields) {   // fill with data from cursor source
			try {
				((CursorField<?>) f.get(model)).fillValue(mDataSource);
			}
			catch (Exception ignored) {
			}
		}
		model.onInitializeFromDS();	// post create task
		return model;
	}
}
