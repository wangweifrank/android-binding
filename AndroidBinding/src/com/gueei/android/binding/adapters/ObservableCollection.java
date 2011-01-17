package com.gueei.android.binding.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.gueei.android.binding.AttributeBinder;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ObservableCollection extends BaseAdapter {
	private int mLayoutId;
	private Context mContext;

	public ObservableCollection(Context context, int layoutId) {
		mContext = context;
		mLayoutId = layoutId;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 90;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ArrayList<View> processedViews;
		View returnView = convertView;
		if ((convertView == null) || ((processedViews = getProcessedViews(convertView))==null)) {
			Binder.InflateResult result = Binder.inflateView(mContext,
					mLayoutId, parent, false);
			processedViews = result.processedViews;
			returnView = result.rootView;
		}
		setPosition(position, processedViews);
		for(View view: processedViews){
			AttributeBinder.getInstance().bindView(view, this);
		}
		return returnView;
	}
	
	public void putProcessedViews(View convertView, ArrayList<View> processedViews){
		convertView.setTag(R.id.tag_observableCollection_processedViews, processedViews);
	}

	public ArrayList<View> getProcessedViews(View convertView){
		Object list = convertView.getTag(R.id.tag_observableCollection_processedViews);
		if ((list==null) || !(list instanceof ArrayList<?>)) return null;
		return (ArrayList<View>)list;
	}
	
	public void setPosition(int position, ArrayList<View> views){
		for(View v: views){
			v.setTag(R.id.tag_observableCollection_position, position);
		}
	}
	
	public int getPosition(View view){
		Object pos = view.getTag(R.id.tag_observableCollection_position);
		if ((pos==null) || !(Integer.class.isInstance(pos))) return -1;
		return (Integer)pos;
	}
	
	//public abstract Object getModel(int position);
	
	public Observable<?> getObservableForName(View view, String propertyName){
		Object collections = view.getTag(R.id.tag_observableCollection_attachedObservable);
		AttachedObservableCollection observables;
		if ((collections==null) || !(collections instanceof AttachedObservableCollection)){
			observables = new AttachedObservableCollection();
			view.setTag(R.id.tag_observableCollection_attachedObservable, observables);
		}else{
			observables = (AttachedObservableCollection)collections;
		}
		MockObservable prop;
		if (observables.containsKey(propertyName)){
			prop = observables.get(propertyName);
		}else{
			prop = new MockObservable(this);
			prop.name = propertyName;
		}
		prop.set("HELLO" + getPosition(view));
		prop.position = getPosition(view);
		return prop;
	}
	
	private static class MockObservable extends Observable{
		public int position;
		public String name;
		private ObservableCollection mCollection;
		
		public MockObservable(ObservableCollection collection){
			mCollection = collection;
		}
	}
	
	private static class AttachedObservableCollection extends HashMap<String, MockObservable>{

		/**
		 * 
		 */
		private static final long serialVersionUID = -2894162138823720056L;}
}
