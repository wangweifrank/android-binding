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

public class IRowModelArrayAdapter<T extends IRowModel> extends BaseAdapter {
	private final Context mContext;
	private final int mLayoutId;
	private final T[] mArray;
	private final CachedModelReflector<T> mReflector;
	private String[] observableNames = new String[0];
	private String[] commandNames = new String[0];
	private String[] valueNames = new String[0];
		
	public IRowModelArrayAdapter(Context context, Class<T> arrayType, T[] array, int layoutId) throws Exception{
		mContext = context;
		mLayoutId = layoutId;
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

	public View getView(int position, View convertView, ViewGroup parent) {
		View returnView = convertView;
		if (position>=mArray.length) return returnView;
		try {
			ObservableMapper<T> mapper;
			if ((convertView == null) || ((mapper = getAttachedMapper(convertView))==null)) {
			//if (true){
				Binder.InflateResult result = Binder.inflateView(mContext,
						mLayoutId, parent, false);
				mapper = new ObservableMapper<T>();
				mapper.initMapping(observableNames, commandNames, valueNames, mReflector, mArray[position]);
				for(View view: result.processedViews){
					AttributeBinder.getInstance().bindView(view, mapper);
				}
				returnView = result.rootView;				
				this.putAttachedMapper(returnView, mapper);
			}
			synchronized(IRowModelArrayAdapter.class){
				T current = mapper.getCurrentMapping();
				mArray[position].onAttachedToUI();
				mapper.changeMapping(mReflector, mArray[position]);
			}
			return returnView;
		} catch (Exception e) {
			e.printStackTrace();
			return returnView;
		}
	}
	
	@SuppressWarnings("unchecked")
	private ObservableMapper<T> getAttachedMapper(View convertView){
		Object mappers = convertView.getTag(R.id.tag_observableCollection_attachedObservable);
		if (mappers==null){
			return null;
		}
		return (ObservableMapper<T>)mappers;
	}
	
	private void putAttachedMapper(View convertView, ObservableMapper<T> mapper){
		convertView.setTag(R.id.tag_observableCollection_attachedObservable, mapper);
	}
}