package com.gueei.demos.markupDemo.viewModels;

import android.util.Log;
import android.view.View;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.collections.ArrayListObservable;
import com.gueei.android.binding.collections.LazyLoadParent;
import com.gueei.android.binding.observables.ArraySource;
import com.gueei.android.binding.observables.StringObservable;

public class MasterDetailListView {
	public final ArrayListObservable<MasterItem> MasterItems = 
		new ArrayListObservable<MasterItem>(MasterItem.class);
	
	public MasterDetailListView(){
		(new Thread(){
			@Override
			public void run() {
				try{
					for(int i=0; i<100; i++){
						MasterItem item = new MasterItem();
						MasterItems.add(item);
						Log.d("Binder", "added item: " + i);
						sleep(2000);
					}
				}catch(Exception e){
					e.printStackTrace();
					Log.i("Binder", "interrupted?");
					return;
				}
			}
		}).start();
	}
	
	public class MasterItem implements LazyLoadParent{
		private int clickCount = 0;
		public final Command ToastTitle = new Command(){
			public void Invoke(View view, Object... args) {
				clickCount ++;
				Title.set("Master: " + clickCount);
			}
		};

		public final StringObservable Title = new StringObservable("Master: ");
		
		public ArraySource<String> DetailItems = new ArraySource<String>();
		public MasterItem(){
		}
		
		public void onLoadChildren() {
			String[] detail = new String[10];
			for (int i=0; i<10; i++){
				detail[i] = "Detail: " + i;
			}
			DetailItems.setArray(detail);
		}
	}
}
