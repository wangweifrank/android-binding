package com.gueei.memoryleak;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;

public class ChildActivityNoBinding extends Activity {
	
	private byte [] buffer;
	public static int BUF_SIZE=1024*1024*10;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_no_binding);
        
        buffer = new byte[BUF_SIZE];
        
        for(int i=0; i < BUF_SIZE; i++) {
        	buffer[i] = (byte) (i % 127);
        }        
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v("ChildNoBindingActivity", "onDestroy");
	}
}
