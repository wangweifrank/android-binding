package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.observables.FloatObservable;
import android.os.Handler;

public class ProgressBar {
	Handler handler = new Handler();
	public ProgressBar(){
		Thread setProgress = new Thread(){
			@Override
			public void run() {
				super.run();
				while(PrimaryProgress.get() < 1.0f){
					handler.post(new Runnable(){
						public void run(){
							PrimaryProgress.set(PrimaryProgress.get() + 0.005f);
							SecondaryProgress.set(PrimaryProgress.get() * 1.5f);							
						}
					});
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
