package gueei.binding.cursor;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;

import java.lang.reflect.Field;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import gueei.binding.R;

public class CursorObservableAdapter<T extends CursorRowModel> extends CursorAdapter {
	protected final CursorObservable<T> mCursorObservable;
	protected int mLayoutId = -1;
	protected int mDropDownLayoutId = -1;
	protected final Context mContext;
	protected Field idField;
	
	/**
	 * Beware this will (deprecated) change in API Level 11
	 */
	public CursorObservableAdapter
		(Context context, CursorObservable<T> cursorObservable, int layoutId, int dropDownLayoutId){
		super(context, cursorObservable.getCursor());
		mContext = context;
		mCursorObservable = cursorObservable;
		mLayoutId = layoutId;
		mDropDownLayoutId = dropDownLayoutId;
	}

	@Override
	public T getItem(int position) {
		T row = constructRow(mContext);
		this.getCursor().moveToPosition(position);
		mCursorObservable.fillData(row, this.getCursor());
		return row;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		T row = getAttachedObservableCollection(view);
		mCursorObservable.fillData(row, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return newView(context, cursor, parent, mLayoutId);
	}

	@Override
	public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
		return newView(context, cursor, parent, mDropDownLayoutId > 0 ? mDropDownLayoutId : mLayoutId);
	}
	
	private View newView(Context context, Cursor cursor, ViewGroup parent, int layoutId){
		try {
			Binder.InflateResult result = Binder.inflateView(context, layoutId, parent, false);
			View returnView = result.rootView;
			T row = constructRow(context);
			for(View view: result.processedViews){
				AttributeBinder.getInstance().bindView(view, row);
			}
			this.putAttachedObservableCollection(returnView, row);
			return returnView;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private T getAttachedObservableCollection(View convertView){
		Object collections = convertView.getTag(R.id.tag_observableCollection_attachedObservable);
		if (collections==null){
			return null;
		}
		return (T)collections;
	}
	
	private void putAttachedObservableCollection(View convertView, T collection){
		convertView.setTag(R.id.tag_observableCollection_attachedObservable, collection);
	}
	
	private T constructRow(Context context){
		return mCursorObservable.newRowModel(context);
	}
}
