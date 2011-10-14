package gueei.binding.cursor;

import android.content.Context;
import gueei.binding.BindingLog;

/**
 * User: =ra=
 * Date: 11.10.11
 * Time: 23:23
 */
@Deprecated // Backward compatibility
public class CursorRowModelFactory implements IRowModelFactory {
	private final Context                         mContext;
	private final Class<? extends CursorRowModel> mRowModelType;

	public CursorRowModelFactory(Class<? extends CursorRowModel> rowModelType, Context context) {
		mRowModelType = rowModelType;
		mContext = context;
	}

	@Override
	public <T extends IRowModel> T createInstance() {
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
