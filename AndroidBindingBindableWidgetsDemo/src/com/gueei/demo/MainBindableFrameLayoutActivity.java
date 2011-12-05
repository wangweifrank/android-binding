package com.gueei.demo;

import com.gueei.demo.framelayout.ConfigChangeDemoActivity;
import com.gueei.demo.framelayout.SimpleDemoActivity;
import com.gueei.demo.framelayout.WizardDemoActivity;

import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainBindableFrameLayoutActivity extends BindingActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.setAndBindRootView(R.layout.bindable_frame_layout_main, this);		
    }
    
	public final Command SimpleDemo = new Command(){
		public void Invoke(View view, Object... args) {		
			Intent intent = new Intent(MainBindableFrameLayoutActivity.this,SimpleDemoActivity.class);
			MainBindableFrameLayoutActivity.this.startActivity(intent);
		}
	};
	
	public final Command WizardDemo = new Command(){
		public void Invoke(View view, Object... args) {		
			Intent intent = new Intent(MainBindableFrameLayoutActivity.this,WizardDemoActivity.class);
			MainBindableFrameLayoutActivity.this.startActivity(intent);
		}
	};
	
	public final Command ConfigChangeDemo = new Command(){
		public void Invoke(View view, Object... args) {		
			Intent intent = new Intent(MainBindableFrameLayoutActivity.this,ConfigChangeDemoActivity.class);
			MainBindableFrameLayoutActivity.this.startActivity(intent);
		}
	};
}
