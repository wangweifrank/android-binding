package com.gueei.android.binding.cursor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.gueei.android.binding.AttributeBinder;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.R;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CursorAdapter<T extends CursorRowModel> extends BaseAdapter {

	private final Cursor mCursor;
	private final Class<T> mRowType;
	private T row;
	private Context mContext;
	private int mLayoutId;
	
	private Field idField;
	private HashMap<String, Field> fields = new HashMap<String, Field>();
	
	public CursorAdapter(Context context, Class<T> rowType, Cursor cursor, int layoutId) throws Exception{
		mRowType = rowType;
		mCursor = cursor;
		mContext = context;
		mLayoutId = layoutId;
		init();
	}
	
	private void init() throws Exception{
		// Create one!
		row = mRowType.newInstance();
		for (Field f : mRowType.getFields()){
			Object field = f.get(row);
			if (!(field instanceof CursorField<?>)) continue;
			if (field instanceof IdField){
				// Should throw exception if more than one id field
				idField = f;
			}
			fields.put(f.getName(), f);
		}
	}
	
	public int getCount() {
		return mCursor.getCount();
	}

	public Object getItem(int position) {
		mCursor.moveToPosition(position);
		return mCursor;
	}

	public long getItemId(int position) {
		if (idField!=null){
			mCursor.moveToPosition(position);
			try {
				return ((IdField)idField.get(row)).returnValue(mCursor);
			} catch (Exception e) {
				return -1;
			}
		}
		return -1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View returnView = convertView;
		try {
			T row;
			if ((convertView == null) || ((row = getAttachedObservableCollection(convertView))==null)) {
				Binder.InflateResult result = Binder.inflateView(mContext,
						mLayoutId, parent, false);
				row = mRowType.newInstance();
				row.setContext(mContext);
				for(View view: result.processedViews){
					AttributeBinder.getInstance().bindView(view, row);
				}
				returnView = result.rootView;
				this.putAttachedObservableCollection(returnView, row);
			}
			mCursor.moveToPosition(position);
			for(Entry<String, Field> entry : fields.entrySet()){
				((CursorField<?>)entry.getValue().get(row)).fillValue(mCursor);
			}
			return returnView;
		} catch (Exception e) {
			e.printStackTrace();
			return returnView;
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
}
