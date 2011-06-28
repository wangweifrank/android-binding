package gueei.binding.collections;

import java.util.AbstractCollection;

import gueei.binding.Binder;
import gueei.binding.IObservable;
import gueei.binding.Observable;
import gueei.binding.Observer;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.SpinnerAdapter;

/**
 * Singleton Adapter is a adapter contains one and only one object
 * @author andy
 */
public class SingletonAdapter implements Adapter, SpinnerAdapter, Observer {
	private final Object mObj;
	private final int mLayoutId;
	private final int mDropDownId;
	private final Context mContext;
	private View mView, mDropDownView;
	private DataSetObserver mDataSetObserver;
	
	public SingletonAdapter(Context context, Object obj, int layoutId, int dropDownLayoutId){
		mObj = obj;
		mContext = context;
		mLayoutId = layoutId;
		mDropDownId = dropDownLayoutId > 0 ? dropDownLayoutId : layoutId;
		if(obj instanceof Observable){
			((Observable<?>) obj).subscribe(this);
		}
	}
	
	public int getCount() {
		return 1;
	}

	public Object getItem(int position) {
		return mObj;
	}

	public long getItemId(int position) {
		return 0;
	}

	public int getItemViewType(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (mView==null){
			mView = Binder.bindView(mContext, 
						Binder.inflateView(mContext, mLayoutId, parent, false),
						mObj);
		}
		return mView;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isEmpty() {
		return false;
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObserver = observer;
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObserver = null;
	}

	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (mDropDownView==null){
			mDropDownView = Binder.bindView(mContext, 
						Binder.inflateView(mContext, mDropDownId, parent, false),
						mObj);
		}
		return mDropDownView;
	}

	public void onPropertyChanged(IObservable<?> prop,
			AbstractCollection<Object> initiators) {
		if (mDataSetObserver!=null)
			mDataSetObserver.notify();
	}
}