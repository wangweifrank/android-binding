package gueei.binding.cursor;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.BindingLog;

import gueei.binding.viewAttributes.templates.Layout;

import java.lang.reflect.Field;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@Deprecated
public class CursorObservableAdapter<T extends CursorRowModel> extends BaseAdapter {
	
	protected final CursorObservable<T> mCursorObservable;
	protected Layout mLayout, mDDLayout;
	protected final Context mContext;
	protected Field idField;

	public CursorObservableAdapter
		(Context context, CursorObservable<T> cursorObservable, Layout layout, Layout ddLayout){
		mContext = context;
		mCursorObservable = cursorObservable;
		mLayout = layout;
		mDDLayout = ddLayout;
	}
	
	@Override
	public int getViewTypeCount() {
		return mLayout.getTemplateCount();
	}

	@Override
	public int getItemViewType(int position) {
		return mLayout.getLayoutTypeId(position);
	}

	public int getCount() {
		if (mCursorObservable.getCursor()==null)
			return 0;
		return mCursorObservable.getCursor().getCount();
	}

	public Object getItem(int position) {
		T row = constructRow(mContext);
		mCursorObservable.getCursor().moveToPosition(position);
		mCursorObservable.fillData(row, mCursorObservable.getCursor());
		return row;
	}

	public long getItemId(int position) {
		return position;
	}
	
	private T constructRow(Context context){
		return mCursorObservable.newRowModel(context);
	}
	
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent, mDDLayout.getLayoutId(position));
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent, mLayout.getLayoutId(position));
	}
	
	private View getView(int position, View convertView, ViewGroup parent, int layoutId) {
		View returnView = convertView;
		Cursor cursor = mCursorObservable.getCursor();
		cursor.moveToPosition(position);

		BindingLog.debug("CursorObservableAdapter", "pos:" + position + ", id:" + layoutId + ", " + cursor.getString(1));
		if (position>=cursor.getCount()) return returnView;
		try {
			if ((convertView == null) || 
					(getAttachedObservableCollection(convertView))==null ||
					(getAttachedViewTypeId(convertView) != layoutId)) {
				returnView = newView(cursor, parent, layoutId);
			}
			bindView(returnView, cursor);
			return returnView;
		} catch (Exception e) {
			e.printStackTrace();
			return returnView;
		}
	}
	
	private static class ObservableCollectionWrapper<T>{
		public T observableCollection;
	}
	
	@SuppressWarnings("unchecked")
	private T getAttachedObservableCollection(View convertView){
		ObservableCollectionWrapper<?> wrapper = 
				Binder.getViewTag(convertView).get(ObservableCollectionWrapper.class);
		if (wrapper!=null)
			return (T)wrapper.observableCollection;
		return null;
	}
	
	private void putAttachedObservableCollection(View convertView, T collection){
		ObservableCollectionWrapper<T> wrapper = 
				new ObservableCollectionWrapper<T>();
		
		wrapper.observableCollection = collection;
		
		Binder.getViewTag(convertView).put(ObservableCollectionWrapper.class, wrapper);
	}
	
	private static class ViewTypeIdWrapper{
		public int viewTypeId;
	}
	
	private int getAttachedViewTypeId(View convertView){
		ViewTypeIdWrapper wrapper = Binder.getViewTag(convertView).get(ViewTypeIdWrapper.class);
		if (wrapper!=null){
			return wrapper.viewTypeId;
		}
		return -1;
	}
	
	
	private void putAttachedViewTypeId(View convertView, int layoutId){
		ViewTypeIdWrapper wrapper = 
				new ViewTypeIdWrapper();
		wrapper.viewTypeId = layoutId;
		Binder.getViewTag(convertView).put(ViewTypeIdWrapper.class, wrapper);
	}

	private void bindView(View view, Cursor cursor) {
		T row = getAttachedObservableCollection(view);
		mCursorObservable.fillData(row, cursor);
	}
	
	private View newView(Cursor cursor, ViewGroup parent, int layoutId){
		try {
			Binder.InflateResult result = Binder.inflateView(mContext, layoutId, parent, false);
			View returnView = result.rootView;
			T row = constructRow(mContext);
			for(View view: result.processedViews){
				AttributeBinder.getInstance().bindView(mContext, view, row);
			}
			this.putAttachedObservableCollection(returnView, row);
			this.putAttachedViewTypeId(returnView, layoutId);
			return returnView;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	/**
	 * Beware this will (deprecated) change in API Level 11
	 *
	public CursorObservableAdapter
		(Context context, CursorObservable<T> cursorObservable, Layout layout, Layout ddLayout){
		super(context, cursorObservable.getCursor());
		mContext = context;
		mCursorObservable = cursorObservable;
		mLayout = layout;
		mDDLayout = ddLayout;
	}

	@Override
	public T getItem(int offset) {
		T row = constructRow(mContext);
		this.getCursor().moveToPosition(offset);
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
		return newView(context, cursor, parent, mLayout.getLayoutId(cursor.getPosition()));
	}

	@Override
	public int getItemViewType(int offset) {
		return mLayout.getLayoutTypeId(offset);
	}

	@Override
	public int getViewTypeCount() {
		return mLayout.getTemplateCount();
	}

	@Override
	public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
		return newView(context, cursor, parent, mDDLayout.getLayoutId(cursor.getPosition()));
	}
	
	private View newView(Context context, Cursor cursor, ViewGroup parent, int layoutId){
		try {
			BinderV30.InflateResult result = BinderV30.inflateView(context, layoutId, parent, false);
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
	*/
}
