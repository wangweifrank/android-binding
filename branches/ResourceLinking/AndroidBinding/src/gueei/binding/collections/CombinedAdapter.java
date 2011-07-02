package gueei.binding.collections;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

public class CombinedAdapter extends BaseAdapter {
	private ArrayList<Adapter> mAdapters = new ArrayList<Adapter>();
	
	public void addAdapter(Adapter adapter){
		mAdapters.add(adapter);
	}

	public int getCount() {
		int count = 0;
		for(Adapter a: mAdapters){
			count += a.getCount();
		}
		return count;
	}

	public Object getItem(int position) {
		TranslatedPosition pos = getTranslatedPosition(position);
		if (pos!=null)
			return pos.adapter.getItem(pos.position);
		return null;
	}

	private TranslatedPosition getTranslatedPosition(int position){
		int count = 0;
		int length = mAdapters.size();
		int index = 0;
		while (index<length){
			Adapter a = mAdapters.get(index);
			if (position< count + a.getCount()){
				return new TranslatedPosition(position - count, a);
			}
			count += a.getCount();
			index ++;
		}
		return null;
	}
	
	public long getItemId(int position) {
		TranslatedPosition pos = getTranslatedPosition(position);
		if (pos!=null)
			return pos.adapter.getItemId(pos.position);
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TranslatedPosition pos = getTranslatedPosition(position);
		if (pos!=null)
			return pos.adapter.getView(pos.position, convertView, parent);
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		TranslatedPosition pos = getTranslatedPosition(position);
		if (pos!=null)
			return mAdapters.indexOf(pos.adapter);
		return -1;
	}

	@Override
	public int getViewTypeCount() {
		int count = 0;
		for(Adapter a: mAdapters){
			count += a.getViewTypeCount();
		}
		return count;
	}

	private class TranslatedPosition{
		public final int position;
		public final Adapter adapter;
		public TranslatedPosition(int pos, Adapter adapter){
			position = pos;
			this.adapter =adapter;
		}
	}
}
