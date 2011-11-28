package com.gueei.demo;

import gueei.binding.Binder;
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
        Binder.init(this.getApplication());   
		this.setAndBindRootView(R.layout.main, this);
	}

	public final Command BindableLinearLayoutDemo = new Command() {
		public void Invoke(View view, Object... args) {
			Intent intent = new Intent(MainActivity.this, BindableLinearLayoutActivity.class);
			MainActivity.this.startActivity(intent);
		}
	};


}
