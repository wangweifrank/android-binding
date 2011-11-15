package com.gueei.demo.abgallery.viewModels;

import android.os.Bundle;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;
import gueei.binding.observables.IntegerObservable;

public class ContentVM implements EventSubscriber{
	public final IntegerObservable ResId = 
			new IntegerObservable(-1);

	public ContentVM(){
		EventAggregator.subscribe("DISPLAYINGIMAGE", this);
	}
	
	public void onEventTriggered(String eventName, Object publisher, Bundle data) {
		if (eventName.equals("DISPLAYINGIMAGE")){
			ResId.set(data.getInt("ID"));
		}
	}
	
	public final Command ToggleSystemUi = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if (view.getSystemUiVisibility() == View.STATUS_BAR_HIDDEN){
				view.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
			}else
				view.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		}
	};
}
