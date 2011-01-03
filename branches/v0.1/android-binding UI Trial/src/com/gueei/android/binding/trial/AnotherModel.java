package com.gueei.android.binding.trial;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;

public class AnotherModel {
	public Observable<CharSequence> hello = new Observable<CharSequence>("BINDING");
	private int goodbyecount = 0;
	public Command goodbyeCommand = new Command() {
		public void Invoke(Object args) {
			hello.set("goodbye: " + goodbyecount++);
		}
	};

	public Observable<CharSequence> enableText = new Observable<CharSequence>("Enable Text");
	public Command toggleEnable = new Command() {
		public void Invoke(Object args){
			enable.set(!enable.get());
		}
	};
	
	public Observable<Boolean> enable = new Observable<Boolean>(true);
}
