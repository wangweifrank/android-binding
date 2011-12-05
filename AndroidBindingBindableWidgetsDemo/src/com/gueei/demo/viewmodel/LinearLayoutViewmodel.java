package com.gueei.demo.viewmodel;

import com.gueei.demo.R;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class LinearLayoutViewmodel {
	public class Item {
		public final StringObservable Name = new StringObservable();
		public final IntegerObservable LayoutId = new IntegerObservable(R.layout.bindable_linear_layout_item);
	}		
	public final ArrayListObservable<Item> Items = new ArrayListObservable<Item>(Item.class);				
}
