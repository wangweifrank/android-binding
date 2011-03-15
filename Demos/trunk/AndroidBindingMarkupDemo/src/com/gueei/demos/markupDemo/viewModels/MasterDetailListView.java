package com.gueei.demos.markupDemo.viewModels;

import android.view.View;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.observables.ArraySource;
import com.gueei.android.binding.observables.StringObservable;

public class MasterDetailListView {
	public final ArraySource<MasterItem> MasterItems = new ArraySource<MasterItem>();
	
	public MasterDetailListView(){
		MasterItem[] master = new MasterItem[50];
		for (int i=0; i<50; i++){
			master[i] = new MasterItem(10);
		}
		MasterItems.setArray(master);
	}
	
	public class MasterItem{
		private int clickCount = 0;
		public final Command ToastTitle = new Command(){
			public void Invoke(View view, Object... args) {
				clickCount ++;
				Title.set("Master: " + clickCount);
			}
		};

		public final StringObservable Title = new StringObservable("Master: ");
		
		public final ArraySource<String> DetailItems = new ArraySource<String>();
		public MasterItem(final int detailCount){
			String[] detail = new String[detailCount];
			for (int i=0; i<detailCount; i++){
				detail[i] = "Detail: " + i;
			}
			DetailItems.setArray(detail);
		}
	}
}
