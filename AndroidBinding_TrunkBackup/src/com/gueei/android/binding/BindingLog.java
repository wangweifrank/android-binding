package com.gueei.android.binding;

import android.util.Log;

public class BindingLog {
	public static final String tag = "Binder";
	
	public static void warning(String occuredAt, String message){
		Log.w(tag, occuredAt + " : " + message);
	}
	
	public static void exception(String occuredAt, Exception e){
		Log.e(tag, occuredAt + " : " + e.getMessage());
	}
}
