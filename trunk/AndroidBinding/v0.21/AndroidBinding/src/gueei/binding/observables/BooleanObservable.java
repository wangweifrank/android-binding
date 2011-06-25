package gueei.binding.observables;

import gueei.binding.Observable;

public class BooleanObservable extends Observable<Boolean> {
	public BooleanObservable() {
		super(Boolean.class);
	}
	
	public BooleanObservable(boolean value){
		super(Boolean.class, value);
	}
}
