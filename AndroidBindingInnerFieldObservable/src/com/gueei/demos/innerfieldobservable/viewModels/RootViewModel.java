package com.gueei.demos.innerfieldobservable.viewModels;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.observables.StringObservable;
import android.view.View;

public class RootViewModel {
	private static int i=0;
	public final StringObservable RootString = new StringObservable("RootString");	
	public final Observable<SubViewModel> SubViewModel = new Observable<SubViewModel>(SubViewModel.class, new SubViewModel());
		
	public final Command UpdateChild = new Command(){
		public void Invoke(View view, Object... args) {
			RootString.set(RootString.get() + (++i));
		}
	};
	
	public final Command NewSubChild = new Command(){
		public void Invoke(View view, Object... args) {
			SubViewModel.set(new SubViewModel());
			
			// TODO: bug
			// you can add the two lines but the binding of the buttons is dead :(
			//SubViewModel.get().SubViewModelString.set(SubViewModel.get().SubViewModelString.get());
			//SubViewModel.get().SubSubViewModel.get().SubSubViewModelString.set(SubViewModel.get().SubSubViewModel.get().SubSubViewModelString.get());
		}
	};
}