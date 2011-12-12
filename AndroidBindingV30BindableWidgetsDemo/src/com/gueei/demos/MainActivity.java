package com.gueei.demos;

import java.util.Arrays;
import java.util.List;

import com.gueei.demo.viewmodel.ViewPagerViewmodel;
import com.gueei.demo.viewmodel.ViewPagerViewmodel.Item;

import gueei.binding.Command;
import gueei.binding.v30.app.BindingActivityV30;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends BindingActivityV30 {

	ViewPagerViewmodel vm = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
        
        createViewModel();
        
        this.setAndBindRootView(R.layout.main, vm); 
		this.setAndBindOptionsMenu(R.menu.optionsmenu, this);
    }
        
	private void createViewModel() {
		vm = new ViewPagerViewmodel();
		/*
		
		for( int k=0; k<3; k++) {
			Item item = (vm).new Item();
			item.Name.set( "Item " + k);
			if( k % 2 == 0)
				item.LayoutId.set(R.layout.pager_item_blue);
			vm.Items.add(item);
		}	
		*/	
	}  
	
	public final Command AddNewItem = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			Item item = (vm).new Item();
			item.Name.set( "Dummy " + vm.Items.size());
			if( vm.Items.size() % 2 == 0)
				item.LayoutId.set(R.layout.pager_item_blue);
			vm.Items.add(item);
		}
	};
	
	public final Command RemoveItemFromTop = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(vm.Items.size() == 0 )
				return;			
			vm.Items.remove(0);
		}
	};
	
	public final Command RemovePos3 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(vm.Items.size() < 4)
				return;			
			vm.Items.remove(3);
		}
	};
	
	public final Command RemoveAll = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			List<?> list = Arrays.asList(vm.Items.toArray());
			vm.Items.removeAll(list);
		}
	};
	
	public final Command Clear = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			vm.Items.clear();
		}
	};
	
	public final Command ChangeLayoutPos2 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(vm.Items.size() < 3)
				return;		
			
			if(vm.Items.get(2).LayoutId.get() == R.layout.pager_item_blue) {
				vm.Items.get(2).LayoutId.set(R.layout.pager_item);
			} else {
				vm.Items.get(2).LayoutId.set(R.layout.pager_item_blue);
			}
		}
	};
	
	
	public final Command Replace3 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(vm.Items.size() < 4)
				return;	
			
			int pos = 3;
			
			Item item = (vm).new Item();
			item.Name.set( "Entry: " + System.currentTimeMillis() );			
			if(vm.Items.get(pos).LayoutId.get() == R.layout.pager_item_blue) {
				item.LayoutId.set(R.layout.pager_item);
			} else {
				item.LayoutId.set(R.layout.pager_item_blue);
			}
			
			vm.Items.replaceItem(pos, item);
		}
	};    	
}