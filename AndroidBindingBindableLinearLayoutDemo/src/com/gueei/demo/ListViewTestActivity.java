package com.gueei.demo;

import gueei.binding.CollectionChangedEventArg;
import gueei.binding.Command;
import gueei.binding.DependentObservable;
import gueei.binding.app.BindingActivity;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.collections.DependentCollectionObservable;
import gueei.binding.observables.IntegerObservable;
import android.os.Bundle;
import android.view.View;

public class ListViewTestActivity extends BindingActivity {
	
	public final ArrayListObservable<ArrayListItem> Items = new ArrayListObservable<ArrayListItem>(ArrayListItem.class);
	@SuppressWarnings("unused")
	private DependentCollectionObservable<Boolean> listIsEmpty = new DependentCollectionObservable<Boolean>(Boolean.class, Items) {
		@Override
		public Boolean calculateValue(CollectionChangedEventArg e, Object... args) throws Exception {
			if( args.length == 0) return false;
			if( !(args[0] instanceof ArrayListObservable<?>))
				return false;		
													
			if( Items == null || Items.size() == 0)
				return true;
			return false;
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                          
        this.setAndBindRootView(R.layout.listviewtest, this); 
    }
    
	public final Command AddItems = new Command() {
		public void Invoke(View view, Object... args) {
			//for (int i = 0; i < 3; i++) {
				Items.add(new ArrayListItem());
			//}
		}
	};  
	
	public final Command Clear = new Command() {
		public void Invoke(View view, Object... args) {
			Items.clear();
		}
	};    
    
    
	public class ArrayListItem {

		private static final String                      Prefix     = "Item: ClickCount=";
		public final         Command                     ClickTitle = new Command() {
			public void Invoke(View view, Object... args) {
				ClickCount.set(ClickCount.get() + 1);
			}
		};
		public final         IntegerObservable           ClickCount = new IntegerObservable(0);
		public final         DependentObservable<String> Title      = new DependentObservable<String>(String.class, ClickCount) {
			@Override
			public String calculateValue(Object... args) throws Exception {
				return Prefix + ClickCount.get();
			}
		};

		@Override public String toString() {
			// simple_spinner_item(2).xml has no binding to "Title"
			return Title.get();
		}
	}
    
}