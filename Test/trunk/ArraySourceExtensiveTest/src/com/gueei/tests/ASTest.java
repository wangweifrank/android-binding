package com.gueei.tests;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.observables.ArraySource;
import com.gueei.android.binding.observables.StringObservable;

import android.app.Activity;
import android.os.Bundle;

public class ASTest extends Activity {
    
	public ArraySource<TestItem> TestList = new ArraySource<TestItem>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestItem[] list = new TestItem[30];
        for (int i=0; i<list.length; i++){
        	list[i] = new TestItem();
        	list[i].TestString.set("item: " + i);
        }
        TestList.setArray(list);
        Binder.setAndBindContentView(this, R.layout.main, this);
    }
    
    public class TestItem{
    	public StringObservable TestString = new StringObservable();
    }
}