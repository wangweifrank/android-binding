package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
import android.app.Activity;
import android.widget.Toast;

public class DynamicLoadingArrayList {
	Activity mActivity;
	public DynamicLoadingArrayList(Activity activity){
		mActivity = activity;
		loadMore();
	}
	
	// Items will be added dynamically to this list
	// when the last item is seen
	public final ArrayListObservable<Item> DynamicLazyList = 
			new ArrayListObservable<Item>(Item.class){
				@Override
				public void onDisplay(int position) {
					super.onDisplay(position);
					this.get(position).Title.set(this.get(position).Batch.get() + position);
					// We reach the end of the list, 
					// try to load more
					if (position>=this.size() - 1){
						loadMore();
					}					
				}

				@Override
				public void onHide(int position) {
					super.onHide(position);
					if (position< this.size()-1)
						this.get(position).Title.set("loading...");
				}
	};
	
	int currentBatch = 0, batchItems = 10;
	public void loadMore(){
		if (IsLoading.get()) return;
		if (!HasMore.get()) return;
		if (currentBatch<5){
			currentBatch ++;
			Toast.makeText(mActivity, "Loading batch: " + currentBatch, Toast.LENGTH_SHORT).show();
			IsLoading.set(true);
			HasMore.set(true);
			// Simulate slow loading of list
			new Thread(){
				@Override
				public void run() {
					for(int i=0; i<batchItems; i++){
						// List collection must be modified in UI Thread. 
						mActivity.runOnUiThread(new Runnable(){
							public void run() {
								DynamicLazyList.add(new Item("Batch" + currentBatch + " : "));
							}
						});
						try {
							sleep(500);
						} catch (InterruptedException e) {
							return;
						}
					}
					// Observables also need to set in UI Thread
					mActivity.runOnUiThread(new Runnable(){
						public void run() {
							IsLoading.set(false);
						}
					});
				}
			}.start();
		}
		else{
			HasMore.set(false);
		}
	}
	
	public final BooleanObservable HasMore = new BooleanObservable(true);
	public final BooleanObservable IsLoading = new BooleanObservable(false);
	
	public static class Item{
		public Item(String batch){
			Batch.set(batch);
		}
		public final StringObservable Title = new StringObservable("loading...");
		public final StringObservable Batch = new StringObservable();
	}
}
