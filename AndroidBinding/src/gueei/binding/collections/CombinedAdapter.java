package gueei.binding.collections;

import java.util.ArrayList;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

public class CombinedAdapter extends BaseAdapter{
	private ArrayList<TranslatedAdapter> mTranslated = new ArrayList<TranslatedAdapter>();

	private DataSetObserver observer = new DataSetObserver(){
		@Override
		public void onChanged() {
			super.onChanged();
			calculateTranslation();
			notifyDataSetChanged();
		}
		
		@Override
		public void onInvalidated() {
			super.onInvalidated();
			notifyDataSetInvalidated();
			calculateTranslation();
		}
	};

	public void addAdapter(Adapter[] adapter){
		for (int i=0; i<adapter.length; i++){
			mTranslated.add(new TranslatedAdapter(adapter[i]));
			adapter[i].registerDataSetObserver(observer);
		}
		
		calculateTranslation();
		notifyDataSetChanged();
	}
	
	/**
	 * Add a new adapter to combined adapter, whenever a new adapter is added, the 
	 * List will be invalidated.
	 * @param adapter
	 */
	public void addAdapter(Adapter adapter){
		mTranslated.add(new TranslatedAdapter(adapter));
		adapter.registerDataSetObserver(observer);
		
		calculateTranslation();
		notifyDataSetChanged();
	}
	
	public void remvoeAdapter(Adapter adapter){
		for(int i=0; i<mTranslated.size(); i++){
			if (mTranslated.get(i).adapter.equals(adapter)){
				mTranslated.remove(i);
				adapter.unregisterDataSetObserver(observer);
				break;
			}
		}
		calculateTranslation();
		notifyDataSetChanged();
	}
	
	private int mItemCount, mItemTypeCount;
	
	private void calculateTranslation(){
		int pos = 0;
		int typeOffset = 0;
		mItemTypeCount = 0;
		mItemCount = 0;
		for(TranslatedAdapter p: mTranslated){
			p.offset = pos;
			p.itemTypeOffset = typeOffset;
			pos += p.adapter.getCount();
			typeOffset += p.adapter.getViewTypeCount();
		}

		mItemCount = pos;
		mItemTypeCount = typeOffset;
	}
	
	public int getCount() {
		return mItemCount;
	}

	public Object getItem(int position) {
		TranslatedAdapter adapter = getAdapterAt(position);
		if (adapter!=null)
			return adapter.adapter.getItem(position - adapter.offset);
		return null;
	}

	private TranslatedAdapter getAdapterAt(int position){
		int length = mTranslated.size();
		TranslatedAdapter adapter; 
		for (int i=0; i<length; i++){
			adapter = mTranslated.get(i);
			if(position >= adapter.offset + adapter.adapter.getCount())
				continue;
			else
				return adapter;
		}
		return null;
	}
	
	public long getItemId(int position) {
		TranslatedAdapter adapter = getAdapterAt(position);
		if (adapter!=null)
			return adapter.adapter.getItemId(position - adapter.offset);
		return -1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TranslatedAdapter adapter = getAdapterAt(position);
		if (adapter!=null)
			return adapter.adapter.getView(position - adapter.offset, convertView, parent);
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		TranslatedAdapter adapter = getAdapterAt(position);
		if (adapter!=null)
			return adapter.itemTypeOffset + adapter.adapter.getItemViewType(position - adapter.offset);
		return BaseAdapter.IGNORE_ITEM_VIEW_TYPE;
	}

	@Override
	public int getViewTypeCount() {
		return mItemTypeCount;
	}

	private class TranslatedAdapter{
		public int offset, itemTypeOffset;
		public final Adapter adapter;
		public TranslatedAdapter(Adapter adapter){
			this.adapter =adapter;
		}
	}
}
