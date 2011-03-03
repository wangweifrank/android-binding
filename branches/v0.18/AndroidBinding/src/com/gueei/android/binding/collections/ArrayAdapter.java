package com.gueei.android.binding.collections;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gueei.android.binding.AttributeBinder;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.R;
import com.gueei.android.binding.utility.CachedModelReflector;

public class ArrayAdapter<T> extends BaseAdapter {
	private final Context mContext;
	private final int mLayoutId;
	private final int mDropDownLayoutId;
	private final T[] mArray;
	private final CachedModelReflector<T> mReflector;
	private String[] observableNames = new String[0];
	private String[] commandNames = new String[0];
	private String[] valueNames = new String[0];
		
	public ArrayAdapter(Context context, Class<T> arrayType, T[] array, int layoutId, int dropDownLayoutId) throws Exception{
		mContext = context;
		mLayoutId = layoutId;
		mDropDownLayoutId = dropDownLayoutId;
		mArray = array;
		mReflector = new CachedModelReflector<T>(arrayType);
		observableNames = mReflector.observables.keySet().toArray(observableNames);
		commandNames = mReflector.commands.keySet().toArray(commandNames);
		valueNames = mReflector.values.keySet().toArray(valueNames);
	}

	public int getCount() {
		return mArray.length;
	}

	public Object getItem(int arg0) {
		return mArray[arg0];
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	private View getView(int position, View convertView, ViewGroup parent, int layoutId) {
		View returnView = convertView;
		if (position>=mArray.length) return returnView;
		try {
			ObservableMapper mapper;
			if ((convertView == null) || ((mapper = getAttachedMapper(convertView))==null)) {
			//if (true){
				Binder.InflateResult result = Binder.inflateView(mContext,
						layoutId, parent, false);
				mapper = new ObservableMapper();
				mapper.initMapping(observableNames, commandNames, valueNames,  mReflector, mArray[position]);
				for(View view: result.processedViews){
					AttributeBinder.getInstance().bindView(view, mapper);
				}
				returnView = result.rootView;				
				this.putAttachedMapper(returnView, mapper);
			}
			synchronized(ArrayAdapter.class){
				mapper.changeMapping(mReflector, mArray[position]);
			}
			return returnView;
		} catch (Exception e) {
			e.printStackTrace();
			return returnView;
		}
	}
	
	private ObservableMapper getAttachedMapper(View convertView){
		Object mappers = convertView.getTag(R.id.tag_observableCollection_attachedObservable);
		if (mappers==null){
			return null;
		}
		return (ObservableMapper)mappers;
	}
	
	private void putAttachedMapper(View convertView, ObservableMapper mapper){
		convertView.setTag(R.id.tag_observableCollection_attachedObservable, mapper);
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return
			mDropDownLayoutId > 0 ?
					getView(position, convertView, parent, mDropDownLayoutId) :
						getView(position, convertView, parent, mLayoutId);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent, mLayoutId);
	}
}