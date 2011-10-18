package com.gueei.demos.bindableframelayoutdemo;

import gueei.binding.Binder;
import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import gueei.extensions.ILayoutLoadEvent;
import android.os.Bundle;
import android.view.View;

public class SimpleDemoActivity extends BindingActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binder.init(this.getApplication());
		this.setAndBindRootView(R.layout.simple, this);		
    }
    
	public final StringObservable HelloWorld = new StringObservable("hello world");	
	public final IntegerObservable LayoutID = new IntegerObservable(0);
	
	public final Command ToggleLayout = new Command(){
		public void Invoke(View view, Object... args) {
			
			if( LayoutID.get() == null || LayoutID.get() == 0) {
				LayoutID.set(R.layout.frame1);
			} else {
				switch( LayoutID.get() ) {
					case R.layout.frame1:
						LayoutID.set(R.layout.frame2);
						break;
					case R.layout.frame2:
						LayoutID.set(R.layout.frame3);
						break;
					default:
						LayoutID.set(null);
						break;				
				}
			}
		}
	};
	
	public final Command OnLoad = new Command(){
		public void Invoke(View view, Object... args) {		
			if( args == null || args.length < 1 )
				return;
			
			ILayoutLoadEvent loader = (ILayoutLoadEvent)args[0];			
			loader.setLayoutId(R.layout.frame_on_load);
		}
	};

}