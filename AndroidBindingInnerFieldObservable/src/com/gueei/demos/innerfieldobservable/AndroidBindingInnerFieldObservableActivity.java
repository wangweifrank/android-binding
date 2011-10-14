package com.gueei.demos.innerfieldobservable;

import com.gueei.demos.innerfieldobservable.viewModels.RootViewModel;

import gueei.binding.Binder;
import gueei.binding.app.BindingActivity;
import gueei.binding.observables.StringObservable;
import android.os.Bundle;

public class AndroidBindingInnerFieldObservableActivity extends BindingActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Binder.init(this.getApplication());
		this.setAndBindRootView(R.layout.main, this, new RootViewModel());
    }
    
	public final StringObservable HelloWorld = new StringObservable("hello world");
}