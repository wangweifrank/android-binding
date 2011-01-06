package com.gueei.android.binding.trial;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.viewFactories.ViewFactory;

public class TrialLaunch extends Activity {
	private LayoutInflater inflater;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = this.getLayoutInflater();
        // inflater.setFactory(this);
		mBinder = new Binder();
		ViewFactory factory = new ViewFactory(mBinder);
		inflater.setFactory(factory);
        setContentView(R.layout.main);
        
        factory.BindView(new AnotherModel());
    }
    private Binder mBinder;
}