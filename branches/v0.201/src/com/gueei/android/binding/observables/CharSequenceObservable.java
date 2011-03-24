package com.gueei.android.binding.observables;

import com.gueei.android.binding.Observable;

public class CharSequenceObservable extends Observable<CharSequence> {
	public CharSequenceObservable() {
		super(CharSequence.class);
	}
	
	public CharSequenceObservable(CharSequence value){
		super(CharSequence.class, value);
	}
}
