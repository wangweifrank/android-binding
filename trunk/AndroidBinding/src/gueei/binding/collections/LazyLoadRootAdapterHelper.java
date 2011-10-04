package gueei.binding.collections;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class LazyLoadRootAdapterHelper implements OnScrollListener {
	// Allow a few items which is close to the viewport to stay
	private int extraItems = 2;
	
	private final AbsListView mView;
	private final LazyLoadAdapter mAdapter;
	private LazyLoadAdapter.Mode mMode;
	private boolean busy;
	
	public LazyLoadRootAdapterHelper(AbsListView view, LazyLoadAdapter adapter, LazyLoadAdapter.Mode mode){
		mAdapter = adapter;
		view.setOnScrollListener(this);
		mMode = mode;
		mView = view;
	}
	
	public void setMode(LazyLoadAdapter.Mode mode){
		mMode = mode;
	}
	
	private int lastFirstVisibleItem, lastVisibleItemCount;
	
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastFirstVisibleItem = firstVisibleItem;
		lastVisibleItemCount = visibleItemCount;
		
		int extraFirst = firstVisibleItem - extraItems < 0 ?
				0: firstVisibleItem - extraItems;
		
		if (!busy)
			mAdapter.onVisibleChildrenChanged(extraFirst, visibleItemCount + extraItems + extraItems);
	}

	public boolean isBusy(){
		return busy;
	}
	
	int lastScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	
	public void onGetView(int position){
		if (!busy)
			mAdapter.onVisibleChildrenChanged(mView.getFirstVisiblePosition(), mView.getLastVisiblePosition() - mView.getFirstVisiblePosition());
	}
	
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mMode.equals(LazyLoadAdapter.Mode.LoadWhenScrolling) 
				|| scrollState==OnScrollListener.SCROLL_STATE_IDLE){
			busy = false;
			mAdapter.onVisibleChildrenChanged(lastFirstVisibleItem, lastVisibleItemCount);
		}else
			busy = true;
	}
}
