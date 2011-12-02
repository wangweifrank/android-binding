package com.gueei.demo.viewmodel;

import java.util.Arrays;
import java.util.List;

import com.gueei.demo.R;

import android.view.View;
import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class LinearLayoutViewmodel {
	public class Item {
		public final StringObservable Name = new StringObservable();
		public final IntegerObservable LayoutId = new IntegerObservable(R.layout.bindable_linear_layout_item);
	}	
	
	public final ArrayListObservable<Item> Items = new ArrayListObservable<Item>(Item.class);
		
	public LinearLayoutViewmodel() {
		for( int k=0; k<3; k++) {
			Item item = new Item();
			item.Name.set( "Item " + k);
			Items.add(item);
		}
	}
    
	public final Command AddNewItem = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			Item item = new Item();
			item.Name.set( "Dummy " + Items.size());
			if( Items.size() % 2 == 0)
				item.LayoutId.set(R.layout.bindable_linear_layout_item_blue);
			Items.add(item);
		}
	};
	
	public final Command RemoveItemFromTop = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(Items.size() == 0 )
				return;			
			Items.remove(0);
		}
	};
	
	public final Command RemovePos3 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(Items.size() < 4)
				return;			
			Items.remove(3);
		}
	};
	
	public final Command RemoveAll = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			List<?> list = Arrays.asList(Items.toArray());
			Items.removeAll(list);
		}
	};
	
	public final Command Clear = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			Items.clear();
		}
	};
	
	public final Command ChangeLayoutPos2 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(Items.size() < 3)
				return;		
			
			if(Items.get(2).LayoutId.get() == R.layout.bindable_linear_layout_item_blue) {
				Items.get(2).LayoutId.set(R.layout.bindable_linear_layout_item);
			} else {
				Items.get(2).LayoutId.set(R.layout.bindable_linear_layout_item_blue);
			}
		}
	};
	
	
	public final Command Replace3 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(Items.size() < 4)
				return;	
			
			int pos = 3;
			
			Item item = new Item();
			item.Name.set( "Entry: " + System.currentTimeMillis() );			
			if(Items.get(pos).LayoutId.get() == R.layout.bindable_linear_layout_item_blue) {
				item.LayoutId.set(R.layout.bindable_linear_layout_item);
			} else {
				item.LayoutId.set(R.layout.bindable_linear_layout_item_blue);
			}
			
			Items.replaceItem(pos, item);
		}
	};
	
	
	
	
}
