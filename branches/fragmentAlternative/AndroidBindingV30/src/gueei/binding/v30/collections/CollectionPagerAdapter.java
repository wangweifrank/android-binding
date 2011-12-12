package gueei.binding.v30.collections;

import java.util.Collection;
import java.util.HashMap;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.BindingSyntaxResolver;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.ConstantObservable;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.InnerFieldObservable;
import gueei.binding.Observer;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.collections.ObservableMapper;
import gueei.binding.utility.CachedModelReflector;
import gueei.binding.utility.IModelReflector;
import gueei.binding.utility.ObservableMultiplexer;
import gueei.binding.viewAttributes.templates.LayoutItem;
import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/////////////////////////////////////////////////////////////////
///
/// note: googles page adapter has some issues - we can't change a view at runtime
/// and expect a change in the displayed items
///
/// to simulate that behaviour, we add a frame layout in the adapter
/// and put our binding elements in the frame layout
///
/////////////////////////////////////////////////////////////////

public class CollectionPagerAdapter extends PagerAdapter 
	implements CollectionObserver {
	
	protected final Handler mHandler;
	protected final Context mContext;
	protected final LayoutItem mLayout;
	protected final IObservableCollection<?> mCollection;
	protected final IModelReflector mReflector;
	
	protected final HashMap<Integer, FrameLayout> mFrameLayouts = new HashMap<Integer, FrameLayout>();

	public CollectionPagerAdapter(Context context, IModelReflector reflector,
			IObservableCollection<?> collection, LayoutItem layout) throws Exception{
		mHandler = new Handler();
		mContext = context;
		mLayout = layout;
		mCollection = collection;
		mReflector = reflector;
		mCollection.subscribe(this);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CollectionPagerAdapter(Context context, IObservableCollection<?> collection, 
			LayoutItem layout) throws Exception{
		this(context, 
				new CachedModelReflector(collection.getComponentType()), collection, layout);
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		ViewPager viewPager = ((ViewPager) collection);
		viewPager.removeView((View) view);			
		mFrameLayouts.remove(position);		
		removeAttachedMapper(collection);	
		
		Object model = mCollection.getItem(position);
		if( model != null )
			observableItemsLayoutID.removeParent(model);
	}

	@Override
	public int getCount() {
		return mCollection.size();
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		ViewPager viewPager = ((ViewPager) collection);
		FrameLayout frameLayout = new FrameLayout(mContext);		
		
		inflateViewInFrameLayout(frameLayout,position);		
		mFrameLayouts.put(position, frameLayout);
		viewPager.addView(frameLayout,position);
		return frameLayout;
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		if( !(object instanceof FrameLayout ))
			return false;
		
		return view == ((FrameLayout) object);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) { }

	@Override
	public void finishUpdate(View arg0) { }
	
	public void onCollectionChanged(IObservableCollection<?> collection, CollectionChangedEventArg args) {
		mHandler.post(new Runnable(){
			public void run(){				
				notifyDataSetChanged();
			}
		});
	}
	
	private void putAttachedMapper(View convertView, ObservableMapper mapper){
		Binder.getViewTag(convertView).put(ObservableMapper.class, mapper);
	}	
	
	private void removeAttachedMapper(View convertView){
		Binder.getViewTag(convertView).remove(ObservableMapper.class);
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int getLayoutId(int position, Object item) {
		int layoutId = mLayout.getLayoutId();		
		if( layoutId < 1 && mLayout.getLayoutName() != null ) {									
			IObservable<?> observable = null;			
			InnerFieldObservable ifo = new InnerFieldObservable(mLayout.getLayoutName());
			if (ifo.createNodes(item)) {
				observable = ifo;										
			} else {			
				Object rawField = BindingSyntaxResolver.getFieldForModel(mLayout.getLayoutName(), item);
				if (rawField instanceof IObservable<?>)
					observable = (IObservable<?>)rawField;
				else if (rawField!=null)
					observable= new ConstantObservable(rawField.getClass(), rawField);
			}
			
			if( observable != null) {	
				observableItemsLayoutID.add(observable, item);	
				Object obj = observable.get();
				if(obj instanceof Integer)
					layoutId = (Integer)obj;
			}
		}	
			
		return layoutId;		
	}
	
	private ObservableMultiplexer<Object> observableItemsLayoutID = new ObservableMultiplexer<Object>(new Observer() {
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {			
			if( initiators == null || initiators.size() < 1)
				return;			
			
			if( !(mCollection instanceof ArrayListObservable<?>))
				return;
			
			Object parent = initiators.toArray()[0];
			int pos = ((ArrayListObservable<?>)mCollection).indexOf(parent);								
			if( pos < 0)
				return;
						
			rebindIfVisible(pos);
		}
	});

	
	private void rebindIfVisible(int position) {
		if( position < 0 || position >= mCollection.size())
			return;
		if( !mFrameLayouts.containsKey(position) )
			return;				
		
		Object model = mCollection.getItem(position);
		if( model != null )
			observableItemsLayoutID.removeParent(model);
				
		FrameLayout frameLayout = mFrameLayouts.get(position);		
		frameLayout.removeAllViews();
		frameLayout.clearDisappearingChildren();
		
		inflateViewInFrameLayout(frameLayout,position);
		
		// this doesn't work - there for our hack with the framelayouts
		//notifyDataSetChanged();		
	}
	
	
	private void inflateViewInFrameLayout(FrameLayout frameLayout, int position) {
		if (position>=mCollection.size()) return;
		try {			
			ObservableMapper mapper;
			
			Object item = mCollection.getItem(position);				
			int layoutId = getLayoutId(position, item);
						
			if( layoutId > 0 ) {
				mCollection.onLoad(position);
									
				Binder.InflateResult result = Binder.inflateView(mContext,
						layoutId, frameLayout, false);
				mapper = new ObservableMapper();
				Object model = mCollection.getItem(position);
				mapper.startCreateMapping(mReflector, model);
				for(View view: result.processedViews){
					AttributeBinder.getInstance().bindView(mContext, view, mapper);
				}
				mapper.endCreateMapping();
				View view = result.rootView;
				this.putAttachedMapper(view, mapper);
				
				mapper.changeMapping(mReflector, item);				
				
				ViewGroup.LayoutParams source = view.getLayoutParams();
				FrameLayout.LayoutParams params = null;
				if( source != null )
					params = new FrameLayout.LayoutParams(source);
				else
					params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
				
				frameLayout.setLayoutParams(params);
				frameLayout.addView(view);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}					
	}
}
