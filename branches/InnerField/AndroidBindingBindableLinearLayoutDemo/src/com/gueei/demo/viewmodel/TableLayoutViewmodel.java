package com.gueei.demo.viewmodel;

import java.util.Arrays;
import java.util.List;

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
		
	public TableLayoutViewmodel() {
		for( int k=0; k<4; k++) {
			Row row = new Row();			
			for( int i=0; i<6; i++) {
				Child child = row.new Child();
				child.Name.set( "Child " + i + " / " + k);
				row.Children.add(child);
			}
			Rows.add(row);
		}
		
		Rows.get(1).Children.get(1).LayoutId.set(R.layout.bindable_table_layout_item_blue);
		
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
			Row row = new Row();			
			for( int i=0; i<6; i++) {
				Child child = row.new Child();
				child.Name.set( Rows.size() + " Child " + i + " / " + Rows.size() + 1);
				if( i % 2 == 0)
					child.LayoutId.set(R.layout.bindable_table_layout_item_blue);					
				row.Children.add(child);
			}
			Rows.add(row);
		}
	};
	
	public final Command RemoveItemFromTop = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(Rows.size() == 0 )
				return;			
			Rows.remove(0);
		}
	};
	
	public final Command RemovePos3 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(Rows.size() < 4)
				return;			
			Rows.remove(3);
		}
	};
	
	public final Command RemoveAll = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			List<?> list = Arrays.asList(Rows.toArray());
			Rows.removeAll(list);
		}
	};
	
	public final Command Clear = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			Rows.clear();
		}
	};
	
	public final Command ChangeLayoutPos2 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(Rows.size() < 3)
				return;		
			
			if( Rows.get(2).Children == null || Rows.get(2).Children.size() < 3 )
				return;
			
			if(Rows.get(2).Children.get(2).LayoutId.get() == R.layout.bindable_table_layout_item_blue) {
				Rows.get(2).Children.get(2).LayoutId.set(R.layout.bindable_table_layout_item);
			} else {
				Rows.get(2).Children.get(2).LayoutId.set(R.layout.bindable_table_layout_item_blue);
			}
		}
	};
	
	
	public final Command Replace3 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(Rows.size() < 4)
				return;	

			int pos = 3;
			
			if( Rows.get(pos).Children == null || Rows.get(pos).Children.size() < 4 )
				return;
						
			Child child = (Rows.get(3)).new Child();
			child.Name.set( "Entry: " + System.currentTimeMillis() );			
			if(Rows.get(pos).Children.get(pos).LayoutId.get() == R.layout.bindable_table_layout_item_blue) {
				child.LayoutId.set(R.layout.bindable_table_layout_item);
			} else {
				child.LayoutId.set(R.layout.bindable_table_layout_item_blue);
			}
			
			Rows.get(pos).Children.replaceItem(pos, child);
		}
	};
	
	
	public final Command Colspan2 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(Rows.size() < 3)
				return;		
			
			if( Rows.get(2).Children == null || Rows.get(2).Children.size() < 3 )
				return;
			
			if(Rows.get(2).Children.get(2).Span.get() != 1 )
				Rows.get(2).Children.get(2).Span.set(1);
			else
				Rows.get(2).Children.get(2).Span.set(3);
		}
	};
	
	
}
