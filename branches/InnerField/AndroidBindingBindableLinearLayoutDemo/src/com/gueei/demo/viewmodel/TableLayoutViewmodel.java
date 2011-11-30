package com.gueei.demo.viewmodel;

import com.gueei.demo.R;
import com.gueei.demo.viewmodel.TableLayoutViewmodel.Row.Child;

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
		}
	}	
	
	public final ArrayListObservable<Row> Rows = new ArrayListObservable<Row>(Row.class);
		
	public TableLayoutViewmodel() {
		for( int k=0; k<3; k++) {
			Row row = new Row();			
			for( int i=0; i<6; i++) {
				Child child = row.new Child();
				child.Name.set( "Child " + i + " / " + k);
				row.Children.add(child);
			}
			Rows.add(row);
		}
		
		Rows.get(1).Children.get(1).LayoutId.set(R.layout.bindable_table_layout_item_1_1);
		
		Row row = new Row();
		row.Children.add(null);
		Child child = row.new Child();
		child.Name.set("Child only pos 2");		
		child.Span.set(2);
		row.Children.add(child);
		Rows.add(row);
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
