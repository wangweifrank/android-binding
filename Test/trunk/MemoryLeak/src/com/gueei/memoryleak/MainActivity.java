package com.gueei.memoryleak;

import gueei.binding.Command;
import gueei.binding.v30.app.BindingActivityV30;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BindingActivityV30 {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndBindRootView(R.layout.activity_main,this);
        setAndBindOptionsMenu(R.menu.activity_main, this);
    }

    public final Command OnClickWithBinding = new Command() {
		
 		@Override
 		public void Invoke(View arg0, Object... arg1) {
 			Intent intent = new Intent(getApplicationContext(), ChildActivityBinding.class);
 			startActivityForResult(intent, 1000);
 		}
 	};
 	
    public final Command OnClickNoBinding = new Command() {
 		
 		@Override
 		public void Invoke(View arg0, Object... arg1) {
 			Intent intent = new Intent(getApplicationContext(), ChildActivityNoBinding.class);
 			startActivityForResult(intent, 2000);
 		}
 	};
    
}
