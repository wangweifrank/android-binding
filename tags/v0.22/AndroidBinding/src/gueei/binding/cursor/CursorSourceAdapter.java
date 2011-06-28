package gueei.binding.cursor;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.R;

import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class CursorSourceAdapter<T extends CursorRowModel> extends CursorAdapter {
	protected final CursorRowTypeMap<T> mRowTypeMap;
	protected int mLayoutId = -1;
	protected int mDropDownLayoutId = -1;
	protected final Context mContext;
	protected Field idField;
	protected WeakHashMap<String, Field> fields = new WeakHashMap<String, Field>();
	
	/**
	 * Beware this will (deprecated) change in API Level 11
	 */
	public CursorSourceAdapter
		(Context context, CursorRowTypeMap<T> rowTypeMap, int layoutId, int dropDownLayoutId){
		super(context, rowTypeMap.getCursor());
		mContext = context;
		mRowTypeMap = rowTypeMap;
		mLayoutId = layoutId;
		mDropDownLayoutId = dropDownLayoutId;
		init();
	}

	@Override
	public Object getItem(int position) {
		T row = constructRow(mContext, this.getCursor());
		for(Entry<String, Field> entry : fields.entrySet()){
			try{
				((CursorField<?>)entry.getValue().get(row)).fillValue(this.getCursor());
			}catch(Exception e){ continue; }
		}
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
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		T row = getAttachedObservableCollection(view);
		for(Entry<String, Field> entry : fields.entrySet()){
			try{
				((CursorField<?>)entry.getValue().get(row)).fillValue(cursor);
			}catch(Exception e){ continue; }
		}
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
			T row;
			Binder.InflateResult result = Binder.inflateView(context, layoutId, parent, false);
			View returnView = result.rootView;
			row = constructRow(context, cursor);
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
	
	private T constructRow(Context context, Cursor cursor){
		T row  = mRowTypeMap.getFactory().createRowModel(context);
		row.setContext(context);
		row.setCursor(cursor);
		return row;
	}
}
