package gueei.binding.collections;

import java.util.ArrayList;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.IObservableCollection;
import gueei.binding.utility.CachedModelReflector;
import gueei.binding.utility.IModelReflector;
import gueei.binding.viewAttributes.templates.Layout;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class CollectionAdapter extends BaseAdapter
	implements CollectionObserver, Filterable, LazyLoadAdapter{
	@Override
	public int getViewTypeCount() {
		return mLayout.getTemplateCount();
	}

	@Override
	public int getItemViewType(int position) {
		return mLayout.getLayoutTypeId(position);
	}

	protected final Handler mHandler;
	protected final Context mContext;
	protected final Layout mLayout, mDropDownLayout;
	protected final IObservableCollection<?> mCollection;
	protected final IModelReflector mReflector;
	protected final Filter mFilter;

	public CollectionAdapter(Context context, IModelReflector reflector,
			IObservableCollection<?> collection, Layout layout, Layout dropDownLayout, Filter filter) throws Exception{
		mHandler = new Handler();
		mContext = context;
		mLayout = layout;
		mDropDownLayout = dropDownLayout;
		mCollection = collection;
		mReflector = reflector;
		mFilter = filter;
		mCollection.subscribe(this);
	}
	
	public CollectionAdapter(Context context, IModelReflector reflector,
			IObservableCollection<?> collection, Layout layout, Layout dropDownLayout) throws Exception{
		this(context, reflector, collection, layout, dropDownLayout, null);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CollectionAdapter(Context context, IObservableCollection<?> collection, 
			Layout layout, Layout dropDownLayout, Filter filter) throws Exception{
		this(context, 
				new CachedModelReflector(collection.getComponentType()), collection, layout, dropDownLayout, filter);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CollectionAdapter(Context context, IObservableCollection<?> collection, 
			Layout layout, Layout dropDownLayout) throws Exception{
		this(context, 
				new CachedModelReflector(collection.getComponentType()), collection, layout, dropDownLayout);		
	}
	
	public void subscribeCollectionObserver(CollectionObserver observer) {
		mCollection.subscribe(observer);
	}

	public void unsubscribeCollectionObserver(CollectionObserver observer) {
		mCollection.unsubscribe(observer);
	}

	public int getCount() {
		return mCollection.size();
	}

	public Object getItem(int position) {
		return mCollection.getItem(position);
	}

	public long getItemId(int position) {
		return mCollection.getItemId(position);
	}

	private View getView(int position, View convertView, ViewGroup parent, int layoutId) {
		View returnView = convertView;
		if (position>=mCollection.size()) return returnView;
		try {
			ObservableMapper mapper;
			
			mCollection.onLoad(position);

			Object item = mCollection.getItem(position);
			
			if (mHelper!=null && !mHelper.isBusy()){
				if (item instanceof LazyLoadRowModel) 
					((LazyLoadRowModel)item).display(mCollection, position);
			}
			
			if ((convertView == null) || 
					((mapper = getAttachedMapper(convertView))==null)) {
				
				Binder.InflateResult result = Binder.inflateView(mContext,
						layoutId, parent, false);
				mapper = new ObservableMapper();
				Object model = mCollection.getItem(position);
				mapper.startCreateMapping(mReflector, model);
				for(View view: result.processedViews){
					AttributeBinder.getInstance().bindView(mContext, view, mapper);
				}
				mapper.endCreateMapping();
				returnView = result.rootView;
				this.putAttachedMapper(returnView, mapper);
			}
			mapper.changeMapping(mReflector, item);
			return returnView;
		} catch (Exception e) {
			e.printStackTrace();
			return returnView;
		}
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent, mDropDownLayout.getLayoutId(position));
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent, mLayout.getLayoutId(position));
	}
	
	private ObservableMapper getAttachedMapper(View convertView){
		return Binder.getViewTag(convertView).get(ObservableMapper.class);
	}
	
	private void putAttachedMapper(View convertView, ObservableMapper mapper){
		Binder.getViewTag(convertView).put(ObservableMapper.class, mapper);
	}
		
	public void onCollectionChanged(IObservableCollection<?> collection, CollectionChangedEventArg args) {
		mHandler.post(new Runnable(){
			public void run(){
				notifyDataSetChanged();
			}
		});
	}

	public Filter getFilter() {
		return mFilter;
	}

	protected Mode mMode = Mode.LoadWhenStopped;
	protected LazyLoadRootAdapterHelper mHelper;
	
	public void setRoot(AbsListView view) {
		if(LazyLoadRowModel.class.isAssignableFrom(mCollection.getComponentType()))
			mHelper = new LazyLoadRootAdapterHelper(view, this, mMode);
	}

	public void setMode(Mode mode) {
		if (mHelper!=null)
		{
			mHelper.setMode(mode);
		}
		mMode = mode;
	}

	private int lastDisplayingFirst = -1, lastTotal = 0;
	
	public void onVisibleChildrenChanged(int first, int total) {
		int collectionSizeLocalCache = mCollection.size();
		total = (collectionSizeLocalCache < total) ? collectionSizeLocalCache : total;

		if (lastTotal != total)
			mCollection.setVisibleChildrenCount(this, total);
		
		int nTotal = total; // > total ? lastTotal : total;
		
		ArrayList<Integer> lastDisplaying = new ArrayList<Integer>();
		for(int i=lastDisplayingFirst; i<lastDisplayingFirst+lastTotal; i++){
			lastDisplaying.add(i);
		}
//		String hide = lastDisplayingFirst + ", " + total + " show: ";
		
		for(int i=first; i<first + nTotal; i++){
			int idx = lastDisplaying.indexOf(i);
			if (idx>=0){
				lastDisplaying.remove(idx);
			}else{
//				hide += i + ", ";
				Object item = mCollection.getItem(i);
				if (item instanceof LazyLoadRowModel)
					((LazyLoadRowModel)item).display(mCollection, i);
			}
		}

		
//		hide += " hide: ";
		
		for(Integer i: lastDisplaying){
			Object item = mCollection.getItem(i);
//			hide += i + ", ";
			if (item instanceof LazyLoadRowModel)
				((LazyLoadRowModel)item).hide(mCollection, i);
		}
		
//		Log.d("Binder", hide);
		
		
		lastDisplayingFirst = first;
		lastTotal = total;
		
		// Don't want to frequently grow and shrink the total size 
//		if (lastTotal < total)
	//		lastTotal = total;
	}
}