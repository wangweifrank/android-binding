package com.gueei.demo.viewmodel;

import android.view.View;

import com.gueei.demos.R;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class ViewPagerViewmodel {
	public class Item {
		public final StringObservable Name = new StringObservable();
		public final IntegerObservable LayoutId = new IntegerObservable(R.layout.pager_item);
		
		public final Command ChangeLayout = new Command(){
			public void Invoke(View view, Object... args) {		
				if( LayoutId.get() == R.layout.pager_item )
					LayoutId.set(R.layout.pager_item_blue);
				else
					LayoutId.set(R.layout.pager_item);
			}
		};
		
	}		 
	public final ArrayListObservable<Item> Items = new ArrayListObservable<Item>(Item.class);				
}
