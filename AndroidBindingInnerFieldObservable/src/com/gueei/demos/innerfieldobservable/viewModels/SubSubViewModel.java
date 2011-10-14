package com.gueei.demos.innerfieldobservable.viewModels;

import gueei.binding.Command;
import gueei.binding.observables.StringObservable;
import android.view.View;

public class SubSubViewModel {	
	private int i=0;
	public final StringObservable SubSubViewModelString = new StringObservable("SubSubViewModelString");
	public final Command UpdateChild = new Command(){
		public void Invoke(View view, Object... args) {
			SubSubViewModelString.set(SubSubViewModelString.get() + (++i));
		}
	};
}