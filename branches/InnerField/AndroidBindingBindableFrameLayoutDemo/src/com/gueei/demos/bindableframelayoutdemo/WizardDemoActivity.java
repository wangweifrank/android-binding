package com.gueei.demos.bindableframelayoutdemo;

import com.gueei.demos.bindableframelayoutdemo.viewmodels.Step1;
import com.gueei.demos.bindableframelayoutdemo.viewmodels.Step2;
import com.gueei.demos.bindableframelayoutdemo.viewmodels.Step3;

import gueei.binding.Binder;
import gueei.binding.Command;
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
        Binder.init(this.getApplication());
        stepLayout();
		this.setAndBindRootView(R.layout.wizard, this);		
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
		if( LayoutID.get() == null || LayoutID.get() == 0 || LayoutID.get() == R.layout.step_static ) {
			LayoutID.set(R.layout.step1);
			DataSource.set(new Step1());
		} else {
			switch( LayoutID.get() ) {
				case R.layout.step1:
					LayoutID.set(R.layout.step2);
					DataSource.set(new Step2());
					break;
				case R.layout.step2:
					LayoutID.set(R.layout.step3);
					DataSource.set(new Step3());
					break;
				default:
					LayoutID.set(R.layout.step_static);
					DataSource.set(null);
					break;				
			}
		}		
	}

}