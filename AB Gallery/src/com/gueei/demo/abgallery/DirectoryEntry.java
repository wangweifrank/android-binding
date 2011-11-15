package com.gueei.demo.abgallery;

import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class DirectoryEntry {
	public final StringObservable Name = new StringObservable();
	public final IntegerObservable ResId = new IntegerObservable();
	
    public DirectoryEntry(String name, int resID) {
    	Name.set(name);
    	ResId.set(resID);
    }
}
