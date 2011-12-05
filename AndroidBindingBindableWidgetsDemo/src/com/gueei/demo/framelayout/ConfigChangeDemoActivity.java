package com.gueei.demo.framelayout;

import com.gueei.demo.R;

import gueei.binding.Observable;
import gueei.binding.app.BindingActivity;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import android.content.res.Configuration;
import android.os.Bundle;

public class ConfigChangeDemoActivity extends BindingActivity {
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);              
		this.setAndBindRootView(R.layout.bindable_frame_layout_configchange, this);
		
		DataSource.set(new Viewmodel());
        setLayoutByConfiguration();
    }
    
    @Override
    public void onConfigurationChanged (Configuration newConfig){
    	super.onConfigurationChanged(newConfig);    	
    	setLayoutByConfiguration();
    	
    	DataSource.get().RotationCounter.set(DataSource.get().RotationCounter.get()+1);
    }       
    
    private void setLayoutByConfiguration() {
    	int orientation = getResources().getConfiguration().orientation;
    	
    	if( orientation == Configuration.ORIENTATION_LANDSCAPE )
    		LayoutID.set(R.layout.bindable_frame_layout_child_landscape);
    	else    		
    		LayoutID.set(R.layout.bindable_frame_layout_child_portrait);    	
	}

		
	public final IntegerObservable LayoutID = new IntegerObservable(0);
	public final Observable<Viewmodel> DataSource = new Observable<Viewmodel>(Viewmodel.class);
	
	
	public class Viewmodel {
		public final IntegerObservable RotationCounter = new IntegerObservable(0);
		public final StringObservable Infotext = new StringObservable("i keep this information even if i am rotated");	
	}
}