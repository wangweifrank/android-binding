package gueei.binding.collections;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.IObservableCollection;
import gueei.binding.utility.CachedModelReflector;
import gueei.binding.utility.EventMarkerHelper;
import gueei.binding.utility.IModelReflector;
import gueei.binding.viewAttributes.adapterView.listView.ItemViewEventMark;
import gueei.binding.viewAttributes.templates.Layout;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

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
	protected final Layout mLayout, mDropDownLayout;
	protected final IObservableCollection<?> mCollection;
	protected final IModelReflector mReflector;
	protected final Filter mFilter;

	public CollectionAdapter(Context context, IModelReflector reflector, IObservableCollection<?> collection, Layout layout, Layout dropDownLayout,
			Filter filter) throws Exception {
		mHandler = new Handler();
		mContext = context;
		mLayout = layout;
		mDropDownLayout = dropDownLayout;
		mCollection = collection;
		mReflector = reflector;
		mFilter = filter;
		mCollection.subscribe(this);
	}

	public CollectionAdapter(Context context, IModelReflector reflector, IObservableCollection<?> collection, Layout layout, Layout dropDownLayout)
			throws Exception {
		this(context, reflector, collection, layout, dropDownLayout, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CollectionAdapter(Context context, IObservableCollection<?> collection, Layout layout, Layout dropDownLayout, Filter filter) throws Exception {
		this(context, new CachedModelReflector(collection.getComponentType()), collection, layout, dropDownLayout, filter);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CollectionAdapter(Context context, IObservableCollection<?> collection, Layout layout, Layout dropDownLayout) throws Exception {
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
		if (position >= mCollection.size())
			return returnView;
		try {
			ObservableMapper mapper;

			mCollection.onLoad(position);

			Object item = mCollection.getItem(position);

			if (mHelper != null && !mHelper.isBusy()) {
				if (item instanceof LazyLoadRowModel)
					((LazyLoadRowModel) item).display(mCollection, position);
			}
			
			ItemViewEventMark mark = new ItemViewEventMark(parent, position, mCollection.getItemId(position));

			if ((convertView == null) || ((mapper = getAttachedMapper(convertView)) == null) || (!mark.equals(EventMarkerHelper.getMark(convertView)))) {
				Binder.InflateResult result = Binder.inflateView(mContext, layoutId, parent, false);
				EventMarkerHelper.mark(result.rootView, mark);
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

	private ObservableMapper getAttachedMapper(View convertView) {
		return Binder.getViewTag(convertView).get(ObservableMapper.class);
	}

	private void putAttachedMapper(View convertView, ObservableMapper mapper) {
		Binder.getViewTag(convertView).put(ObservableMapper.class, mapper);
	}

	public void onCollectionChanged(IObservableCollection<?> collection, CollectionChangedEventArg args) {
		mHandler.post(new Runnable() {

			public void run() {
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
		if (LazyLoadRowModel.class.isAssignableFrom(mCollection.getComponentType()))
			mHelper = new LazyLoadRootAdapterHelper(view, this, mMode);
	}

	public void setMode(Mode mode) {
		if (mHelper != null) {
			mHelper.setMode(mode);
		}
		mMode = mode;
	}

	private int lastDisplayingFirst = -1;
	private int lastTotal = 0;

	public void onVisibleChildrenChanged(int first, int total) {
		int actualCollectionSize = mCollection.size();
		if (0 == actualCollectionSize) {
			// nothing to show, nothing to hide, reset last* 
			lastDisplayingFirst = -1;
			lastTotal = 0;
			return;
		}
		// normalize NEW first and last indexes
		// normalize newTotal, should be greater or equal to 0
		int newTotal = total < 0 ? 0 : total;
		// normalize newFirstIndex, should be less than actualCollectionSize
		int newFirstIndex = (first >= actualCollectionSize) ? actualCollectionSize - 1 : first;
		// normalize newFirstIndex, should be greater or equal to 0
		newFirstIndex = newFirstIndex < 0 ? 0 : newFirstIndex;
		// calculate last new visible index
		int newLastIndex = newFirstIndex + newTotal;
		// normalize newLastIndex, should be equal or greater than less than newFirstIndex
		newLastIndex = (newLastIndex < newFirstIndex) ? newFirstIndex + 1 : newLastIndex;
		// normalize newLastIndex, should be less than actualCollectionSize
		newLastIndex = (newLastIndex > actualCollectionSize) ? actualCollectionSize : newLastIndex;

		// normalize OLD first and last indexes (collection size can be changed between function calls)
		// normalize oldTotal, should be greater or equal to 0
		int oldTotal = lastTotal < 0 ? 0 : lastTotal;
		// normalize oldFirstIndex, should be less or equal to actualCollectionSize
		int oldFirstIndex = (lastDisplayingFirst > actualCollectionSize - 1) ? actualCollectionSize - 1 : lastDisplayingFirst;
		// normalize oldFirstIndex, should be greater or equal to 0
		oldFirstIndex = oldFirstIndex < 0 ? 0 : oldFirstIndex;
		// calculate last old visible index
		int oldLastIndex = oldFirstIndex + oldTotal;
		// normalize oldLastIndex, should be equal or greater than less than oldFirstIndex
		oldLastIndex = (oldLastIndex < oldFirstIndex) ? oldFirstIndex + 1 : oldLastIndex;
		// normalize oldLastIndex, should be less or equal to actualCollectionSize
		oldLastIndex = (oldLastIndex > actualCollectionSize) ? actualCollectionSize : oldLastIndex;

		LazyLoadRowModel item;

		for (int i = newFirstIndex; i < oldFirstIndex; ++i) {
			item = (LazyLoadRowModel) mCollection.getItem(i);
			item.display(mCollection, i);
		}
		for (int i = oldFirstIndex; i < newFirstIndex; ++i) {
			item = (LazyLoadRowModel) mCollection.getItem(i);
			item.hide(mCollection, i);
		}

		for (int i = newLastIndex; i < oldLastIndex; ++i) {
			item = (LazyLoadRowModel) mCollection.getItem(i);
			item.hide(mCollection, i);
		}
		for (int i = oldLastIndex; i < newLastIndex; ++i) {
			item = (LazyLoadRowModel) mCollection.getItem(i);
			item.display(mCollection, i);
		}

		// set lastDisplayingFirst and lastTotal;
		lastDisplayingFirst = newFirstIndex;
		lastTotal = newTotal;
	}
}