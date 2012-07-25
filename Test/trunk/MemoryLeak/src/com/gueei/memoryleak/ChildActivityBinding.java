package com.gueei.memoryleak;

import android.os.Bundle;

import gueei.binding.observables.StringObservable;
import gueei.binding.v30.app.BindingActivityV30;
import android.util.Log;

public class ChildActivityBinding extends BindingActivityV30 {
	
	private byte [] buffer;
	
	public final StringObservable MemSize = new StringObservable("");
	public static int BUF_SIZE=1024*1024*10;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndBindRootView(R.layout.activity_child_binding, this);
        
        buffer = new byte[BUF_SIZE];
        
        for(int i=0; i < BUF_SIZE; i++) {
        	buffer[i] = (byte) (i % 127);
        }
        
        MemSize.set(""+BUF_SIZE);
        
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v("ChildBindingActivity", "onDestroy");
	}
    
    
    

}
