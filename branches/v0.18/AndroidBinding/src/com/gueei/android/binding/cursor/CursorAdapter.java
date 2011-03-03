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
	private Context mContext;
	private int mLayoutId = -1;
	private int mDropDownLayoutId = -1;
	private Field idField;
	private HashMap<String, Field> fields = new HashMap<String, Field>();
	
	public CursorAdapter(Context context, CursorRowTypeMap<T> rowTypeMap, int layoutId, int dropDownLayoutId){
		mRowTypeMap = rowTypeMap;
		mCursor = rowTypeMap.getCursor();
		mContext = context;
		mLayoutId = layoutId;
		mDropDownLayoutId = dropDownLayoutId;
		init();
	}
	
	private T constructRow(){
		T row  = mRowTypeMap.getFactory().createRowModel(mContext);
		row.setContext(mContext);
		row.setCursor(mCursor);
		return row;
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
				return ((IdField)idField.get(constructRow())).returnValue(mCursor);
			} catch (Exception e) {
				return -1;
			}
		}
		return -1;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return
			mDropDownLayoutId > 0 ?
					getView(position, convertView, parent, mDropDownLayoutId) :
						getView(position, convertView, parent, mLayoutId);
	}

	private View getView(int position, View convertView, ViewGroup parent, int layoutId){
		View returnView = convertView;
		try {
			T row;
			if ((convertView == null) || ((row = getAttachedObservableCollection(convertView))==null)) {
				Binder.InflateResult result = Binder.inflateView(mContext,
						layoutId, parent, false);
				returnView = result.rootView;
				row = constructRow();
				for(View view: result.processedViews){
					AttributeBinder.getInstance().bindView(view, row);
				}
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
	
	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent, mLayoutId);
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
