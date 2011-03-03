package com.gueei.demos.markupDemo.viewModels;

import com.gueei.android.binding.observables.FloatObservable;

public class ProgressBar {
	public ProgressBar(){
		Thread setProgress = new Thread(){
			@Override
			public void run() {
				super.run();
				while(PrimaryProgress.get() < 1.0f){
					PrimaryProgress.set(PrimaryProgress.get() + 0.005f);
					SecondaryProgress.set(PrimaryProgress.get() * 1.5f);
					try{
						sleep(50);
					}catch(Exception e){
						return;
					}
				}
			}
		};
		setProgress.start();
	}
	
	public final FloatObservable PrimaryProgress = new FloatObservable(0f);
	public final FloatObservable SecondaryProgress = new FloatObservable(0f);
}
