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
        showRAM();
    }
        
    private void showRAM() {    	
    	long max = Runtime.getRuntime().maxMemory();
    	long heapSize = Runtime.getRuntime().totalMemory();
    	long freeSize = Runtime.getRuntime().freeMemory();
    	long allocSize = heapSize - freeSize;
    	
    	
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("max: ");
    	sb.append(max / 1024L);
    	sb.append("kb ");
    	sb.append("heap: ");
    	sb.append(heapSize / 1024L);
    	sb.append("kb ");
    	sb.append("alloc: ");
    	sb.append(allocSize / 1024L);
    	sb.append("kb ");
    	sb.append("free: ");
    	sb.append(freeSize / 1024L);
    	sb.append("kb ");
    	
		Log.v("showRAM", sb.toString());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		showRAM();
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
