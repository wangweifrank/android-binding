package com.gueei.demo;

import java.util.Arrays;
import java.util.List;

import com.gueei.demo.viewmodel.TableLayoutViewmodel;
import com.gueei.demo.viewmodel.TableLayoutViewmodel.Row;
import com.gueei.demo.viewmodel.TableLayoutViewmodel.Row.Child;

import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import android.os.Bundle;
import android.view.View;

public class MainBindableTableLayoutActivity extends BindingActivity {
	
	TableLayoutViewmodel vm = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        
        createViewModel();
                
        this.setAndBindRootView(R.layout.bindable_table_layout, vm);
        this.setAndBindOptionsMenu(R.menu.table_layout, this);
    }

	private void createViewModel() {
		
		vm = new TableLayoutViewmodel();
		
		for( int k=0; k<4; k++) {
			Row row = (vm).new Row();			
			for( int i=0; i<6; i++) {
				Child child = row.new Child();
				child.Name.set( "C: " + i + " / " + k);
				row.Children.add(child);
			}
			vm.Rows.add(row);
		}
		
		vm.Rows.get(1).Children.get(1).LayoutId.set(R.layout.bindable_table_layout_item_blue);
		
		Row row = (vm).new Row();
		row.Children.add(null);
		Child child = row.new Child();
		child.Name.set("span2");		
		child.Span.set(2);
		row.Children.add(child);
		row.Children.add(null);
		row.Children.add(child);
		vm.Rows.add(row);	
		
	}
	
	public final Command AddNewItem = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			Row row = (vm).new Row();			
			for( int i=0; i<6; i++) {
				Child child = row.new Child();
				child.Name.set( vm.Rows.size() + " Child " + i + " / " + vm.Rows.size() + 1);
				if( i % 2 == 0)
					child.LayoutId.set(R.layout.bindable_table_layout_item_blue);					
				row.Children.add(child);
			}
			vm.Rows.add(row);
		}
	};
	
	public final Command RemoveItemFromTop = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(vm.Rows.size() == 0 )
				return;			
			vm.Rows.remove(0);
		}
	};
	
	public final Command RemovePos3 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(vm.Rows.size() < 4)
				return;			
			vm.Rows.remove(3);
		}
	};
	
	public final Command RemoveAll = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			List<?> list = Arrays.asList(vm.Rows.toArray());
			vm.Rows.removeAll(list);
		}
	};
	
	public final Command Clear = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			vm.Rows.clear();
		}
	};
	
	public final Command ChangeLayoutPos2 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(vm.Rows.size() < 3)
				return;		
			
			if( vm.Rows.get(2).Children == null || vm.Rows.get(2).Children.size() < 3 )
				return;
			
			if(vm.Rows.get(2).Children.get(2).LayoutId.get() == R.layout.bindable_table_layout_item_blue) {
				vm.Rows.get(2).Children.get(2).LayoutId.set(R.layout.bindable_table_layout_item);
			} else {
				vm.Rows.get(2).Children.get(2).LayoutId.set(R.layout.bindable_table_layout_item_blue);
			}
		}
	};
	
	
	public final Command Replace3 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(vm.Rows.size() < 4)
				return;	

			int pos = 3;
			
			if( vm.Rows.get(pos).Children == null || vm.Rows.get(pos).Children.size() < 4 )
				return;
						
			Child child = (vm.Rows.get(3)).new Child();
			child.Name.set( "Entry: " + System.currentTimeMillis() );			
			if(vm.Rows.get(pos).Children.get(pos).LayoutId.get() == R.layout.bindable_table_layout_item_blue) {
				child.LayoutId.set(R.layout.bindable_table_layout_item);
			} else {
				child.LayoutId.set(R.layout.bindable_table_layout_item_blue);
			}
			
			vm.Rows.get(pos).Children.replaceItem(pos, child);
		}
	};
	
	
	public final Command Colspan2 = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if(vm.Rows.size() < 3)
				return;		
			
			if( vm.Rows.get(2).Children == null || vm.Rows.get(2).Children.size() < 3 )
				return;
			
			if(vm.Rows.get(2).Children.get(2).Span.get() != 1 )
				vm.Rows.get(2).Children.get(2).Span.set(1);
			else
				vm.Rows.get(2).Children.get(2).Span.set(3);
		}
	};
	
	
    
}