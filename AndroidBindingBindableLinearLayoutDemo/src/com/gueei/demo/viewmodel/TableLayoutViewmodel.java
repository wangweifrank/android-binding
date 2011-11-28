package com.gueei.demo.viewmodel;

import android.view.View;
import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.StringObservable;

public class TableLayoutViewmodel {
	public class Row {
		public final StringObservable Name = new StringObservable();
	}	
	
	public final ArrayListObservable<Row> Rows = new ArrayListObservable<Row>(Row.class);
		
	public TableLayoutViewmodel() {
		for( int k=0; k<3; k++) {
			Row row = new Row();
			row.Name.set( "Row " + k);
			Rows.add(row);
		}
	}
    
	public final Command AddNewItem = new Command(){
		@Override
		public void Invoke(View view, Object... args) {

		}
	};
	
	public final Command RemoveItemFromTop = new Command(){
		@Override
		public void Invoke(View view, Object... args) {

		}
	};
	
	public final Command RemovePos3 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {

		}
	};
	
}
