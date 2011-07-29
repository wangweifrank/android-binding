package com.gueei.demos.markupDemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.LongObservable;
import gueei.binding.observables.StringObservable;
import gueei.binding.serialization.NoParcel;
import gueei.binding.serialization.ViewModelParceler;

public class Parceling extends BindingActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState!=null){
			long start = System.currentTimeMillis();
			ViewModelParceler.restoreViewModel(savedInstanceState.getBundle("vm"), this);
			Toast.makeText
				(this, 
						"Restore state took: " + (System.currentTimeMillis() - start) + " ms", 
						Toast.LENGTH_SHORT).show();
		}
		this.setAndBindRootView(R.layout.parceling, this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBundle("vm", ViewModelParceler.parcelViewModel(this));
	}
	
	@NoParcel
	public final StringObservable NoParcelString = new StringObservable("create time: " + System.currentTimeMillis());
	
	public final StringObservable SaveString = new StringObservable("create time: " + System.currentTimeMillis());
	public final LongObservable SaveLong = new LongObservable(System.currentTimeMillis());
	public final BooleanObservable SaveBoolean = new BooleanObservable(false);
	
	public final ArrayListObservable<String> SaveArrayList = 
			new ArrayListObservable<String>(String.class, new String[]{
				"item: " + System.currentTimeMillis(),
				"item: " + System.currentTimeMillis(),
				"item: " + System.currentTimeMillis(),
				"item: " + System.currentTimeMillis()
			});
}
