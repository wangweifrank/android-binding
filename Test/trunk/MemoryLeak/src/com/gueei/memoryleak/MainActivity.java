package com.gueei.memoryleak;

import gueei.binding.Command;
import gueei.binding.v30.app.BindingActivityV30;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BindingActivityV30 {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndBindRootView(R.layout.activity_main,this);
        setAndBindOptionsMenu(R.menu.activity_main, this);
        Tools.showRAM();
    }
        


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Tools.showRAM();
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
 	
    public final Command OnClickBindableFramelayout = new Command() {
 		
 		@Override
 		public void Invoke(View arg0, Object... arg1) {
 			Intent intent = new Intent(getApplicationContext(), ChildActivityBindableFramelayout.class);
 			startActivityForResult(intent, 3000);
 		}
 	}; 	
 	
 	
    
}
