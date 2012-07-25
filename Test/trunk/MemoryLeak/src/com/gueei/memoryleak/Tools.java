package com.gueei.memoryleak;

import android.util.Log;

public class Tools {
    public static void showRAM() {    	
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
}
