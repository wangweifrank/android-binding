package com.gueei.demos.markupDemo.viewModels;

import android.view.View;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.observables.BooleanObservable;
import com.gueei.android.binding.observables.StringObservable;

public class TextView {
	public final StringObservable FirstName = new StringObservable("");
	public final StringObservable LastName = new StringObservable("");
	public final BooleanObservable Changed = new BooleanObservable(false);
	public final Command TextChanging = new Command(){
		private Thread resetChange;
		public void Invoke(View view, Object... args) {
			Changed.set(true);
			if ((resetChange!=null)&&(resetChange.isAlive()))
				resetChange.interrupt();
			resetChange = new Thread(){
				@Override
				public void run() {
					try{
						sleep(1000);
						Changed.set(false);
					}catch(Exception e){
						return;
					}
				}
			};
			resetChange.start();
		}
	};
}