package com.example.binding.demo;

import java.util.AbstractCollection;

import com.gueei.android.binding.AttributeBinder;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Converter;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Demo1 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Binder binder = new Binder();
        Binder.init();
        Binder.setAndBindContentView(this, R.layout.demo1, new DemoModel1(this));
        //Binder.setAndBindContentView(this, R.layout.demo1code, new Demo1CodeModel(this));
    }
}