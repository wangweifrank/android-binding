package com.gueei.memoryleak;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import gueei.binding.v30.app.BindingActivityV30;
import android.os.Bundle;
import android.view.View;


public class ChildActivityBindableFramelayout extends BindingActivityV30 {
	
	public final Observable<ChildVM> DataSource = new Observable<ChildVM>(ChildVM.class);
	public final IntegerObservable LayoutId = new IntegerObservable(R.layout.bindable_framelayout);
	
	public final Command OnToggle = new Command() {
		
		@Override
		public void Invoke(View view, Object... args) {
			if(LayoutId.get() == R.layout.bindable_framelayout ) {
				DataSource.set(null);
				LayoutId.set(R.layout.empty);
			} else {
				DataSource.set(new ChildVM(0));
				LayoutId.set(R.layout.bindable_framelayout);
			}
			
			 Tools.showRAM();
		}
	};
	
	public static class ChildVM {
		
		public final IntegerObservable LayoutId = new IntegerObservable(0);
		public final IntegerObservable Color = new IntegerObservable(R.color.ms_alice_blue);

		public final Observable<ChildVM> Child1 = new Observable<ChildVM>(ChildVM.class);
		public final Observable<ChildVM> Child2 = new Observable<ChildVM>(ChildVM.class);
		public final Observable<ChildVM> Child3 = new Observable<ChildVM>(ChildVM.class);
		public final Observable<ChildVM> Child4 = new Observable<ChildVM>(ChildVM.class);
		
		private byte [] buffer;		
		public static int BUF_SIZE=1024*1024*10; // 10mb
		
		public ChildVM(int instance) {	
			
			if(instance == 0) {
		        buffer = new byte[BUF_SIZE];		        
		        for(int i=0; i < BUF_SIZE; i++) {
		        	buffer[i] = (byte) (i % 127);
		        }
			}			
			
			instance++;
			if(instance<5) {
				Child1.set(new ChildVM(instance));
				Child2.set(new ChildVM(instance));
				Child3.set(new ChildVM(instance));
				Child4.set(new ChildVM(instance));				
				
				LayoutId.set(R.layout.bindable_framelayout);
				
				if(instance == 1) {
					Color.set(R.color.ms_gray);
				} else if(instance == 2) {
					Color.set(R.color.ms_green);
				} else if(instance == 3) {
					Color.set(R.color.ms_yellow);
				} else if(instance == 4) {
					Color.set(R.color.ms_red);
				}
			}			
			

			
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataSource.set(new ChildVM(0));        
        setAndBindRootView(R.layout.activity_child_bindable_framelayout, this);
   	 	Tools.showRAM();
    }

}
