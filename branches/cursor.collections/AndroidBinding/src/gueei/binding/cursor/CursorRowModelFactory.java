package gueei.binding.cursor;

import android.content.Context;
import gueei.binding.BindingLog;

/**
 * User: =ra=
 * Date: 11.10.11
 * Time: 23:23
 */
@Deprecated // Backward compatibility
public class CursorRowModelFactory<T extends CursorRowModel> implements IRowModelFactory<T> {
	private final Context                         mContext;
	private final Class<T> mRowModelType;

	public CursorRowModelFactory(Class<T> rowModelType, Context context) {
		mRowModelType = rowModelType;
		mContext = context;
	}

	@Override
	public T createInstance() {
		try {
			//noinspection unchecked
			CursorRowModel model = mRowModelType.newInstance();
			model.setContext(mContext);
			return (T) model;
		} catch (Exception e) {
			BindingLog.exception("CursorRowModelFactory.createInstance", e);
			return null;
		}
	}
}
