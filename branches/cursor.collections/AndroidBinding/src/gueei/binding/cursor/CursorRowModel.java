package gueei.binding.cursor;

import gueei.binding.IObservableCollection;
import android.content.Context;
import android.database.Cursor;

@Deprecated
public abstract class CursorRowModel implements IRowModel {
	@Deprecated
	public interface Factory<T extends CursorRowModel> {
		public T createRowModel(Context context);
	}

	private Context context;
	private Cursor  cursor;

	public Cursor getCursor() {
		return cursor;
	}

	@Deprecated
	void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	@Deprecated
	public CursorRowModel() {}

	@Deprecated
	public Context getContext() {
		return context;
	}

	@Deprecated
	public void setContext(Context context) {
		this.context = context;
	}

	@Deprecated
	public void onLoad(int position) {}

	// rowId or something unique for cursors row set
	@Override
	public long getId(int proposedId) {
		return proposedId;
	}

	@Override
	public void onInitialize() {}

	@Override
	public void display(IObservableCollection<?> collection, int index) {
	}

	@Override
	public void hide(IObservableCollection<?> collection, int index) {
	}
}
