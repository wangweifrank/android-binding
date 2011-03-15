package com.gueei.android.binding.collections;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListAdapter;

import com.gueei.android.binding.AttributeBinder;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.IObservable;
import com.gueei.android.binding.R;
import com.gueei.android.binding.utility.CachedModelReflector;

public class ExpandableArrayAdapter<T> extends ArrayAdapter<T> implements ExpandableListAdapter{
	private final String mChildName;
	private final int mChildLayoutId;
	private final ArrayAdapter<?>[] mChildAdapters;

	public ExpandableArrayAdapter(Context context, Class<T> arrayType,
			T[] array, int layoutId, int dropDownLayoutId, String childName, int childLayoutId)
			throws Exception {
		super(context, arrayType, array, layoutId, dropDownLayoutId);
		mChildName = childName;
		mChildLayoutId = childLayoutId;
		mChildAdapters = new ArrayAdapter<?>[array.length];
	}

	private ArrayAdapter<?> getChildAdapter(int groupPosition){
		try{
			if (mChildAdapters[groupPosition]==null){
				IObservable<?> child = mReflector.getObservableByName(mChildName, mArray[groupPosition]);
				mChildAdapters[groupPosition] = 
					new ArrayAdapter(mContext, child.get().getClass(), (Object[])child.get(), mChildLayoutId, -1);
			}
			return mChildAdapters[groupPosition];
		}catch(Exception e){
			return null;
		}
	}
	
	public Object getChild(int groupPosition, int childPosition) {
		return getChildAdapter(groupPosition).getItem(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return getChildAdapter(groupPosition).getItemId(childPosition);
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		return getChildAdapter(groupPosition).getView(childPosition, convertView, parent);
	}

	public int getChildrenCount(int groupPosition) {
		return getChildAdapter(groupPosition).getCount();
	}

	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getGroup(int groupPosition) {
		return getItem(groupPosition);
	}

	public int getGroupCount() {
		return getCount();
	}

	public long getGroupId(int groupPosition) {
		return getItemId(groupPosition);
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		return getView(groupPosition, convertView, parent);
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		
	}
}