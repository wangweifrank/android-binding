package com.gueei.android.binding.demo;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.viewFactories.ViewFactory;

import android.app.Activity;
import android.os.Bundle;

public class Demo1 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binder binder = new Binder();
        ViewFactory factory = new ViewFactory(binder);
        this.getLayoutInflater().setFactory(factory);
        this.setContentView(R.layout.demo1);
        factory.BindView(new DemoModel1(this));
    }
}