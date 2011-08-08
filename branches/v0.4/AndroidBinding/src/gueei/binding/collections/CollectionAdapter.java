package gueei.binding.collections;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.CollectionObserver;
import gueei.binding.IObservableCollection;
import gueei.binding.utility.CachedModelReflector;
import gueei.binding.utility.IModelReflector;
import gueei.binding.viewAttributes.templates.Layout;

// TODO: disable if empty
public class CollectionAdapter extends BaseAdapter implements CollectionObserver, Filterable, LazyLoadAdapter {

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
	protected final Layout  mLayout, mDropDownLayout;
	protected final IObservableCollection<?> mCollection;
	protected final IModelReflector          mReflector;
	protected final Filter                   mFilter;

	public CollectionAdapter(Context context, IModelReflector reflector, IObservableCollection<?> collection,
							 Layout layout, Layout dropDownLayout, Filter filter) throws Exception {
		mHandler = new Handler();
		mContext = context;
		mLayout = layout;
		mDropDownLayout = dropDownLayout;
		mCollection = collection;
		mReflector = reflector;
		mFilter = filter;
		mCollection.subscribe(this);
	}

	public CollectionAdapter(Context context, IModelReflector reflector, IObservableCollection<?> collection,
							 Layout layout, Layout dropDownLayout) throws Exception {
		this(context, reflector, collection, layout, dropDownLayout, null);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public CollectionAdapter(Context context, IObservableCollection<?> collection, Layout layout,
							 Layout dropDownLayout,
							 Filter filter) throws Exception {
		this(context, new CachedModelReflector(collection.getComponentType()), collection, layout, dropDownLayout,
			 filter);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public CollectionAdapter(Context context, IObservableCollection<?> collection, Layout layout,
							 Layout dropDownLayout) throws Exception {
		this(context, new CachedModelReflector(collection.getComponentType()), collection, layout, dropDownLayout);
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
		if (position >= mCollection.size()) {
			return returnView;
		}
		try {
			ObservableMapper mapper;
			mCollection.onLoad(position);
			if (mHelper != null && !mHelper.isBusy()) {
				((LazyLoadCollection) mCollection).onDisplay(position);
			}
			if ((convertView == null) || ((mapper = getAttachedMapper(convertView)) == null)) {
				Binder.InflateResult result = Binder.inflateView(mContext, layoutId, parent, false);
				mapper = new ObservableMapper();
				Object model = mCollection.getItem(position);
				mapper.startCreateMapping(mReflector, model);
				for (View view : result.processedViews) {
					AttributeBinder.getInstance().bindView(mContext, view, mapper);
				}
				mapper.endCreateMapping();
				returnView = result.rootView;
				this.putAttachedMapper(returnView, mapper);
			}
			mapper.changeMapping(mReflector, mCollection.getItem(position));
			return returnView;
		}
		catch (Exception e) {
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

	private ObservableMapper getAttachedMapper(View convertView) {
		return Binder.getViewTag(convertView).get(ObservableMapper.class);
	}

	private void putAttachedMapper(View convertView, ObservableMapper mapper) {
		Binder.getViewTag(convertView).put(ObservableMapper.class, mapper);
	}

	public void onCollectionChanged(IObservableCollection<?> collection) {
		mHandler.post(new Runnable() {
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

	public Filter getFilter() {
		return mFilter;
	}

	private Mode mMode = Mode.LoadWhenStopped;
	private LazyLoadRootAdapterHelper mHelper;

	public void setRoot(AbsListView view) {
		if (mCollection instanceof LazyLoadCollection) {
			mHelper = new LazyLoadRootAdapterHelper(view, this, mMode);
		}
	}

	public void setMode(Mode mode) {
		if (mHelper != null) {
			mHelper.setMode(mode);
		}
		mMode = mode;
	}

	private int lastDisplayingFirst = 0, lastTotal = 0;

	public void onVisibleChildrenChanged(int first, int total) {
		if (total <= 0) {
			return;
		}
		if (lastDisplayingFirst == first) {
			if (total == lastTotal) {
				return;
			}
			if (lastTotal < total) {
				for (int i = first + lastTotal; i < first + total; i++) {
					((LazyLoadCollection) mCollection).onDisplay(i);
				}
			}
		}
		else {
			if (lastDisplayingFirst < first) {
				int offCount = first - lastDisplayingFirst;
				for (int i = first + total - offCount; i < first + total; i++) {
					((LazyLoadCollection) mCollection).onDisplay(i);
				}
				for (int i = lastDisplayingFirst; i < lastDisplayingFirst + offCount; i++) {
					((LazyLoadCollection) mCollection).onHide(i);
				}
			}
			else {
				int offCount = lastDisplayingFirst - first;
				for (int i = first; i < first + offCount; i++) {
					((LazyLoadCollection) mCollection).onDisplay(i);
				}
				for (int i = first + total; i < lastDisplayingFirst + total; i++) {
					((LazyLoadCollection) mCollection).onHide(i);
				}
			}
		}
		lastTotal = total;
		lastDisplayingFirst = first;
	}
}
