package com.gueei.android.binding.converters;

import android.widget.Adapter;

import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.IObservable;

public class COMBINE extends DependentObservable<Adapter>{
	public COMBINE(IObservable<?>[] dependents) {
		super(Adapter.class, dependents);
	}

	@Override
	public Adapter calculateValue(Object... args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
