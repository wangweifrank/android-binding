package com.gueei.demos.bindableframelayoutdemo;


import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BindingActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.setAndBindRootView(R.layout.main, this);		
    }
    
	public final Command SimpleDemo = new Command(){
		public void Invoke(View view, Object... args) {		
			Intent intent = new Intent(MainActivity.this,SimpleDemoActivity.class);
			MainActivity.this.startActivity(intent);
		}
	};
	
	public final Command WizardDemo = new Command(){
		public void Invoke(View view, Object... args) {		
			Intent intent = new Intent(MainActivity.this,WizardDemoActivity.class);
			MainActivity.this.startActivity(intent);
		}
	};
	
	public final Command ConfigChangeDemo = new Command(){
		public void Invoke(View view, Object... args) {		
			Intent intent = new Intent(MainActivity.this,ConfigChangeDemoActivity.class);
			MainActivity.this.startActivity(intent);
		}
	};
}
