package gueei.binding.collections;

import gueei.binding.IObservable;

import java.util.WeakHashMap;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;


public class ExpandableCollectionAdapter extends BaseExpandableListAdapter{
	private final String mChildName;
	private final int mChildLayoutId;
	private final WeakHashMap<Integer, Adapter> mChildAdapters =
		new WeakHashMap<Integer, Adapter>();
	private final Adapter mGroupAdapter;
	private final Context mContext;

	public ExpandableCollectionAdapter(Context context, Adapter groupAdapter, String childName, int childLayoutId){
		mChildName = childName;
		mChildLayoutId = childLayoutId;
		mContext = context;
		mGroupAdapter = groupAdapter;
	}
	/*
	public ExpandableCollectionAdapter(Context context,
			IObservableCollection<?> collection, int layoutId, int dropDownLayoutId, String childName, int childLayoutId)
			throws Exception {
		mChildName = childName;
		mChildLayoutId = childLayoutId;
		mContext = context;
		mGroupAdapter = new CollectionAdapter(context, collection, layoutId, dropDownLayoutId);
	}
*/
	
	private Adapter getChildAdapter(int groupPosition){
		Log.d("Binder", "Get Child Adapter " + groupPosition);
		try{
			if (!mChildAdapters.containsKey(groupPosition)){
				Object item = mGroupAdapter.getItem(groupPosition);
				if (item instanceof LazyLoadParent){
					((LazyLoadParent)item).onLoadChildren();
				}
				IObservable<?> child = 
					gueei.binding.Utility.getObservableForModel(mChildName, item);
				mChildAdapters.put(groupPosition, 
					Utility.getSimpleAdapter(mContext, child.get(), mChildLayoutId, -1));
			}
			return mChildAdapters.get(groupPosition);
		}catch(Exception e){
			e.printStackTrace();
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

	public Object getGroup(int groupPosition) {
		return mGroupAdapter.getItem(groupPosition);
	}

	public int getGroupCount() {
		return mGroupAdapter.getCount();
	}

	public long getGroupId(int groupPosition) {
		return mGroupAdapter.getItemId(groupPosition);
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		return mGroupAdapter.getView(groupPosition, convertView, parent);
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void onGroupCollapsed(int groupPosition) {
		mChildAdapters.remove(groupPosition);
	}

	public void onGroupExpanded(int groupPosition) {
	}

	public boolean hasStableIds() {
		return true;
	}
}