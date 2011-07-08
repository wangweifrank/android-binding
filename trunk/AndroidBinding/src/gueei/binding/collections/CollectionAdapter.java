package gueei.binding.collections;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.CollectionObserver;
import gueei.binding.IObservableCollection;
import gueei.binding.R;
import gueei.binding.utility.CachedModelReflector;
import gueei.binding.utility.IModelReflector;
import gueei.binding.viewAttributes.templates.Layout;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CollectionAdapter extends BaseAdapter
	implements CollectionObserver{
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
	//protected final int mLayoutId;
	//protected final int mDropDownLayoutId;
	protected final IObservableCollection<?> mCollection;
	protected final IModelReflector mReflector;
		
	public CollectionAdapter(Context context, IModelReflector reflector,
			IObservableCollection<?> collection, Layout layout, Layout dropDownLayout) throws Exception{
		mHandler = new Handler();
		mContext = context;
		mLayout = layout;
		mDropDownLayout = dropDownLayout;
		mCollection = collection;
		mReflector = reflector;
		collection.subscribe(this);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CollectionAdapter(Context context, IObservableCollection<?> collection, 
			Layout layout, Layout dropDownLayout) throws Exception{
		this(context, 
				new CachedModelReflector(collection.getComponentType()), collection, layout, dropDownLayout);		
	}
	
	public int getCount() {
		return mCollection.size();
	}

	public Object getItem(int position) {
		return mCollection.getItem(position);
	}

	public long getItemId(int position) {
		return position;
	}

	private View getView(int position, View convertView, ViewGroup parent, int layoutId) {
		View returnView = convertView;
		if (position>=mCollection.size()) return returnView;
		try {
			ObservableMapper mapper;
			if ((convertView == null) || 
					((mapper = getAttachedMapper(convertView))==null)) {
				
				Binder.InflateResult result = Binder.inflateView(mContext,
						layoutId, parent, false);
				mapper = new ObservableMapper();
				Object model = mCollection.getItem(position);
				mCollection.onLoad(position);
				mapper.startCreateMapping(mReflector, model);
				for(View view: result.processedViews){
					AttributeBinder.getInstance().bindView(view, mapper);
				}
				mapper.endCreateMapping();
				returnView = result.rootView;
				this.putAttachedMapper(returnView, mapper);
			}else{
				mCollection.onLoad(position);
			}
			mapper.changeMapping(mReflector, mCollection.getItem(position));
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
		Object mappers = convertView.getTag(R.id.tag_observableCollection_attachedObservable);
		if (mappers==null){
			return null;
		}
		return (ObservableMapper)mappers;
	}
	
	private void putAttachedMapper(View convertView, ObservableMapper mapper){
		convertView.setTag(R.id.tag_observableCollection_attachedObservable, mapper);
	}
		
	public void onCollectionChanged(IObservableCollection<?> collection) {
		mHandler.post(new Runnable(){
			public void run(){
				notifyDataSetChanged();
			}
		});
	}
}