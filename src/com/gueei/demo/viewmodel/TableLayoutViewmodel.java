package com.gueei.demo.viewmodel;

import com.gueei.demo.R;

import android.view.View;
import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class TableLayoutViewmodel {
	public class Row {		
		public final ArrayListObservable<Child> Children = new ArrayListObservable<Child>(Child.class);		
		public class Child {
			public final StringObservable Name = new StringObservable();
			public final IntegerObservable LayoutId = new IntegerObservable(R.layout.bindable_table_layout_item);
			public final IntegerObservable Span = new IntegerObservable(1);
			
			public final Command Clicked = new Command(){
				@Override
				public void Invoke(View view, Object... args) {
					if(LayoutId.get() == R.layout.bindable_table_layout_item_blue) {
						LayoutId.set(R.layout.bindable_table_layout_item);
					} else {
						LayoutId.set(R.layout.bindable_table_layout_item_blue);
					}
				}
			};
		}
	}	
	
	public final ArrayListObservable<Row> Rows = new ArrayListObservable<Row>(Row.class);
		
}
