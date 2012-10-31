package gueei.binding.cursor;

import gueei.binding.IObservable;
import gueei.binding.collections.LazyLoadParent;
import gueei.binding.collections.Utility;
import gueei.binding.viewAttributes.templates.Layout;

import java.util.Hashtable;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;


@Deprecated
public class ExpandableCursorAdapter<T extends CursorRowModel> extends BaseExpandableListAdapter{
	
	private final String mChildName;
	private final Layout mChildLayout;
	private final CursorObservableAdapter<T> mCursorAdapter; 
	private volatile Hashtable<Integer, Adapter> mChildAdapters;
	private final Context mContext;

	public ExpandableCursorAdapter(Context context,
			CursorObservable<T> cursorObservable, Layout layout, Layout ddLayout,
			String childName, Layout childLayout) {
		mChildName = childName;
		mChildLayout = childLayout;
		mChildAdapters = new Hashtable<Integer, Adapter>();
		Cursor cursor = cursorObservable.getCursor();
		DataSetObserver cursorObserver;
		cursorObserver = new DataSetObserver() {
				@Override
				public void onChanged() {
					super.onChanged();
					mChildAdapters.clear();
				}
			};
		cursor.registerDataSetObserver(cursorObserver);
		mCursorAdapter = new CursorObservableAdapter<T>
			(context, cursorObservable, layout, ddLayout);
		mContext = context;
	}

	private synchronized Adapter getChildAdapter(int groupPosition){
		Log.d("BinderV30", "Get Child Adapter " + groupPosition);
		synchronized(this){
			if (mChildAdapters.containsKey(groupPosition))
				return mChildAdapters.get(groupPosition);
			try{
				Object item = mCursorAdapter.getItem(groupPosition);
				if (item instanceof LazyLoadParent){
					((LazyLoadParent)item).onLoadChildren(mContext);
				}
				IObservable<?> child = gueei.binding.Utility
					.getObservableForModel(mContext, mChildName, item);
				//noinspection NullableProblems
				mChildAdapters.put(groupPosition,
					Utility.getSimpleAdapter(mContext, child.get(), mChildLayout, mChildLayout, null));

				return mChildAdapters.get(groupPosition);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public Object getChild(int groupPosition, int childPosition) {
		return getChildAdapter(groupPosition).getItem(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return getChildAdapter(groupPosition).getItemId(childPosition);
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		return getChildAdapter(groupPosition).getView(groupPosition, convertView, parent);
	}

	public int getChildrenCount(int groupPosition) {
		return getChildAdapter(groupPosition).getCount();
	}

	public Object getGroup(int groupPosition) {
		return mCursorAdapter.getItem(groupPosition);
	}

	public int getGroupCount() {
		return mCursorAdapter.getCount();
	}

	public long getGroupId(int groupPosition) {
		return mCursorAdapter.getItemId(groupPosition);
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		return mCursorAdapter.getView(groupPosition, convertView, parent);
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
		synchronized(this){
			mChildAdapters.remove(groupPosition);
		}
	}
}
