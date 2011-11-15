package com.gueei.demo.abgallery.viewModels;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;
import gueei.binding.observables.IntegerObservable;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gueei.demo.abgallery.ContentActivity;
import com.gueei.demo.abgallery.Directory;
import com.gueei.demo.abgallery.DirectoryEntry;
import com.gueei.demo.abgallery.MainActivity;

public class TitlesVM implements EventSubscriber{
	private Activity mActivity;
	public TitlesVM(Activity activity){
		Directory.initializeDirectory();
		Entries.setArray(Directory.getCategory(0).Entries.clone());
		DisplayingIndex.set(0);
		EventAggregator.subscribe("DISPLAYINGCATEGORY", this);
		mActivity = activity;
	}
	
	public final ArrayListObservable<DirectoryEntry> Entries = 
    		new ArrayListObservable<DirectoryEntry>(DirectoryEntry.class);
	
	public final IntegerObservable DisplayingIndex = 
    		new IntegerObservable();
	
	public final Command DisplayPicture = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			Bundle bundle = new Bundle();
			bundle.putInt("ID", Entries.getItem(DisplayingIndex.get()).ResId.get());
			EventAggregator.publish("DISPLAYINGIMAGE", TitlesVM.this, bundle);
		}
	};
	
	public final Command DisplayPictureInNewActivity = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			//TODO: make new Content Activity
			Intent intent = new Intent(mActivity, ContentActivity.class);
			intent.putExtra("ID", Entries.getItem(DisplayingIndex.get()).ResId.get());
			mActivity.startActivity(intent);
		}
	};

	public void onEventTriggered(String eventName, Object publisher, Bundle data) {
		if (eventName.equals("DISPLAYINGCATEGORY")){
			int cat = data.getInt("INDEX");
			Entries.setArray(Directory.getCategory(cat).Entries.clone());
			DisplayingIndex.set(0);
			DisplayPicture.Invoke(null);
		}
	}
}
