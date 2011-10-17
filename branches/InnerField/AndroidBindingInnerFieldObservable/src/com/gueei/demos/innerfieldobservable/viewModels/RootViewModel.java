package com.gueei.demos.innerfieldobservable.viewModels;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.observables.StringObservable;
import android.util.Log;
import android.view.View;

public class RootViewModel {
	private int i=0;
	public final StringObservable RootString = new StringObservable("RootString");	
	public final Observable<SubViewModel> SubViewModel 
		= new Observable<SubViewModel>(SubViewModel.class);
		
	public final Command UpdateChild = new Command(){
		public void Invoke(View view, Object... args) {
			RootString.set(RootString.get() + (++i));
		}
	};
	
	public final Command NewSubChild = new Command(){
		public void Invoke(View view, Object... args) {
			SubViewModel.set(new SubViewModel());
		}
	};
	
	public final Command ClearSubChild = new Command(){
		public void Invoke(View view, Object... args) {
			SubViewModel.set(null);
			// test if the GC starts the finalizer for the sub child
			System.gc();
		}
	};
	
	protected void finalize() {
		Log.v("innerField", "RootViewModel.finalize");
	}
}