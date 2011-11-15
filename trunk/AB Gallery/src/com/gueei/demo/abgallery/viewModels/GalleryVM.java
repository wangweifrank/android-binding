package com.gueei.demo.abgallery.viewModels;

import gueei.binding.Command;
import gueei.binding.labs.EventAggregator;
import gueei.binding.observables.IntegerObservable;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.gueei.demo.abgallery.R;

public class GalleryVM {
	private Activity mActivity;

	public GalleryVM(Activity activity) {
		super();
		mActivity = activity;
	}
	
	// Titles may not be needed in some layout (e.g. when small screen)
	public final LazyLoadVM<TitlesVM> Titles = 
    		new LazyLoadVM<TitlesVM>(TitlesVM.class){
			@Override
			protected TitlesVM initValue() {
				return new TitlesVM(mActivity);
			}
	};
	
	// Content may not be needed in some layout 
	public final LazyLoadVM<ContentVM> Content = 
    		new LazyLoadVM<ContentVM>(ContentVM.class){
			@Override
			protected ContentVM initValue() {
				return new ContentVM();
			}
	};
	
	public final Command TitleSelected = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			Bundle bundle = new Bundle();
			bundle.putInt("INDEX", (Integer)args[0]);
			EventAggregator.publish("DISPLAYINGCATEGORY", GalleryVM.this, bundle);
		}
	};
	
	public final IntegerObservable ThemeId = 
			new IntegerObservable(R.style.AppTheme_Dark);
	
	public final Command ToggleTheme = new Command(){
    	@Override
    	public void Invoke(View view, Object... args) {
			if (ThemeId.get() == R.style.AppTheme_Light){
				ThemeId.set(R.style.ActionBar_Dark);
			}else{
				ThemeId.set(R.style.AppTheme_Light);
			}
			mActivity.recreate();
		}
    };
}
