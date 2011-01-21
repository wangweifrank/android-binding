package com.gueei.android.binding.cursor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gueei.android.binding.AttributeBinder;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.R;

public class CursorAdapter<T extends CursorRowModel> extends BaseAdapter {

	private final Cursor mCursor;
	private final CursorRowTypeMap<T> mRowTypeMap;
	private T row;
	private Context mContext;
	private int mLayoutId;
	
	private Field idField;
	private HashMap<String, Field> fields = new HashMap<String, Field>();
	
	public CursorAdapter(Context context, CursorRowTypeMap<T> rowTypeMap, int layoutId){
		mRowTypeMap = rowTypeMap;
		mCursor = rowTypeMap.getCursor();
		mContext = context;
		mLayoutId = layoutId;
		init();
	}
	
	private T constructRow(){
		try {
			T row  = mRowTypeMap.getRowType().newInstance();
			row.setParameters(mRowTypeMap.getInjectParameters());
			row.setContext(mContext);
			row.setCursor(mCursor);
			return row;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void init(){
		for (Field f :mRowTypeMap.getRowType().getFields()){
			if (!CursorField.class.isAssignableFrom(f.getType())) continue;
			if (IdField.class.isAssignableFrom(f.getType())){
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
		T row;
		try {
			row = constructRow();
			row.setContext(mContext);
			for(Entry<String, Field> entry : fields.entrySet()){
				((CursorField<?>)entry.getValue().get(row)).fillValue(mCursor);
			}
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
				row = constructRow();
				for(View view: result.processedViews){
					AttributeBinder.getInstance().bindView(view, row);
				}
				returnView = result.rootView;
				this.putAttachedObservableCollection(returnView, row);
			}
			row.resetInternalState(position);
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
