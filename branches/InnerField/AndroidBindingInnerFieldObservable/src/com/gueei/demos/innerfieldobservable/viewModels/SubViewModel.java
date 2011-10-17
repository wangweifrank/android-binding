package com.gueei.demos.innerfieldobservable.viewModels;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.observables.StringObservable;
import android.util.Log;
import android.view.View;

public class SubViewModel {	
	private int i=0;
	public final StringObservable SubViewModelString = new StringObservable("SubViewModelString");
	public final Observable<SubSubViewModel> SubSubViewModel = new Observable<SubSubViewModel>(SubSubViewModel.class, new SubSubViewModel());
	
	public final Command UpdateChild = new Command(){
		public void Invoke(View view, Object... args) {
			SubViewModelString.set(SubViewModelString.get() + (++i));
		}
	};
	
	protected void finalize() {
		Log.v("innerField", "SubViewModel.finalize");
	}
}