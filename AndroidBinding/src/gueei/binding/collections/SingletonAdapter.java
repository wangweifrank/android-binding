package gueei.binding.collections;

import gueei.binding.Binder;
import gueei.binding.IObservable;
import gueei.binding.Observable;
import gueei.binding.Observer;
import gueei.binding.viewAttributes.templates.LayoutTemplate;

import java.util.Collection;

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
	private int mLayoutId;
	private int mDropDownId;
	private final Context mContext;
	private View mView, mDropDownView;
	private DataSetObserver mDataSetObserver;
	private Observer templateObserver = new Observer(){
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			mLayoutId = ((LayoutTemplate)prop).getTemplate();
		}
	};
	private Observer dropDownTemplateObserver = new Observer(){
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			mDropDownId = ((LayoutTemplate)prop).getTemplate();
		}
	};
	
	public SingletonAdapter(Context context, Object obj, LayoutTemplate template, LayoutTemplate dropDownTemplate){
		mObj = obj;
		mContext = context;
		mLayoutId = template.getTemplate();
		mDropDownId = dropDownTemplate.getTemplate();
		
		template.subscribe(templateObserver);
		dropDownTemplate.subscribe(dropDownTemplateObserver);
		
		if(obj instanceof Observable){
			((Observable<?>) obj).subscribe(this);
		}		
	}
	
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
			Collection<Object> initiators) {
		if (mDataSetObserver!=null)
			mDataSetObserver.notify();
	}
}