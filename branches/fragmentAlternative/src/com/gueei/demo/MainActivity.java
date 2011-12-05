package com.gueei.demo;

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
    
	public final Command FrameLayout = new Command(){
		public void Invoke(View view, Object... args) {		
			Intent intent = new Intent(MainActivity.this,MainBindableFrameLayoutActivity.class);
			MainActivity.this.startActivity(intent);
		}
	};
	
	public final Command LinearLayout = new Command(){
		public void Invoke(View view, Object... args) {		
			Intent intent = new Intent(MainActivity.this,MainBindableLinearLayoutActivity.class);
			MainActivity.this.startActivity(intent);
		}
	};
	
	public final Command TableLayout = new Command(){
		public void Invoke(View view, Object... args) {		
			Intent intent = new Intent(MainActivity.this,MainBindableTableLayoutActivity.class);
			MainActivity.this.startActivity(intent);
		}
	};
}