package com.gueei.demos.markupDemo.viewModels;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.observables.BooleanObservable;

public class View {
	public Command Toggle = new Command(){
		public void Invoke(android.view.View view, Object... args) {
			TextVisible.set(!TextVisible.get());
		}
	};
	public BooleanObservable TextVisible = new BooleanObservable(true);
}
