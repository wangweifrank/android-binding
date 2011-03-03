package com.gueei.android.binding.converters;

import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.IObservable;

public class CONCAT extends DependentObservable<String> {

	public CONCAT(IObservable<?>[] dependents) {
		super(String.class, dependents);
	}

	@Override
	public String calculateValue(Object... args) throws Exception {
		int len = args.length;
		String result = "";
		for(int i=0; i<len; i++){
			result += args[i].toString();
		}
		return result;
	}
}
