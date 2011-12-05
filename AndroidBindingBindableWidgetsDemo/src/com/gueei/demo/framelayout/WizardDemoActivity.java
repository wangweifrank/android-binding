package com.gueei.demo.framelayout;

import com.gueei.demo.R;
import com.gueei.demo.viewmodel.FrameLayoutStep1;
import com.gueei.demo.viewmodel.FrameLayoutStep2;
import com.gueei.demo.viewmodel.FrameLayoutStep3;

import gueei.binding.Command;
import gueei.binding.Debugger;
import gueei.binding.Observable;
import gueei.binding.app.BindingActivity;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import android.os.Bundle;
import android.view.View;

public class WizardDemoActivity extends BindingActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stepLayout();
		this.setAndBindRootView(R.layout.bindable_frame_layout_wizard, this);		
    }
    
    public final StringObservable HelloWorld = new StringObservable("hello world");	
	public final IntegerObservable LayoutID = new IntegerObservable(0);
	public final Observable<Object> DataSource = new Observable<Object>(Object.class, new Object());
		
	public final Command OnStep = new Command(){
		public void Invoke(View view, Object... args) {		
			stepLayout();
		}
	};
	
	void stepLayout() {
		Debugger.graphObject(DataSource.get(), 10, null, null);
		if( LayoutID.get() == null || LayoutID.get() == 0 || LayoutID.get() == R.layout.bindable_frame_layout_step_static ) {
			LayoutID.set(R.layout.bindable_frame_layout_step1);
			DataSource.set(new FrameLayoutStep1());
		} else {
			switch( LayoutID.get() ) {
				case R.layout.bindable_frame_layout_step1:
					LayoutID.set(R.layout.bindable_frame_layout_step2);
					DataSource.set(new FrameLayoutStep2());
					break;
				case R.layout.bindable_frame_layout_step2:
					LayoutID.set(R.layout.bindable_frame_layout_step3);
					DataSource.set(new FrameLayoutStep3());
					break;
				default:
					LayoutID.set(R.layout.bindable_frame_layout_step_static);
					DataSource.set(null);
					break;				
			}
		}		
	}

}