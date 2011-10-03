package com.gueei.demo.abgallery;

import gueei.binding.Command;
import gueei.binding.DependentObservable;
import gueei.binding.Observable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.v30.actionbar.ActionBarBinder;
import gueei.binding.v30.app.BindingActivityV30;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BindingActivityV30 {
	// Work around for setting system ui visibility... 
	// no idea why this is implemented in View rather than other topper level stuff... 
	private View rootView;

	private int mThemeId = 0; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState!=null){
	        mThemeId = savedInstanceState.getInt("THEME_ID");
	        if (mThemeId>0){
	        	setTheme(mThemeId);
	        }
        }
        
        Directory.initializeDirectory();
        rootView = this.getLayoutInflater().inflate(R.layout.main, null);
        setContentView(rootView);
        
        this.setAndBindOptionsMenu(R.menu.main_menu, this);
        
        ActionBarBinder.BindActionBar(this, R.xml.main_metadata, this);
    }
    
    
	public final BooleanObservable TitleVisible = new BooleanObservable(true);

	public final Command TitleSelected = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			for(int i=0; i<Directory.getCategoryCount(); i++){
				if (Directory.getCategory(i).getName().equals(args[0])){
					CurrentCategory.set(Directory.getCategory(i));
					DisplayingIndex.set(0);
				}
			}
		}
    };

    public final Command ToggleTitles = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			//toggleVisibleTitles();
		}
    };

    public final Command ToggleTheme = new Command(){
    	@Override
    	public void Invoke(View view, Object... args) {
			if (mThemeId==R.style.AppTheme_Light){
				mThemeId = R.style.ActionBar_Dark;
			}else{
				mThemeId = R.style.AppTheme_Light;
			}
			recreate();
		}
    };
    

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("THEME_ID", mThemeId);
	}

	public final Observable<DirectoryCategory> CurrentCategory = 
    		new Observable<DirectoryCategory>(DirectoryCategory.class);

    public final IntegerObservable DisplayingIndex = 
    		new IntegerObservable(0);
    
    public final DependentObservable<DirectoryEntry> DisplayingImage =
    		new DependentObservable<DirectoryEntry>(DirectoryEntry.class, DisplayingIndex){
				@Override
				public DirectoryEntry calculateValue(Object... args) throws Exception {
					return CurrentCategory.get().getEntry(DisplayingIndex.get());
				}
    };
    
    public final Command ToggleSystemUi = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if (rootView.getSystemUiVisibility()==View.STATUS_BAR_VISIBLE){
				rootView.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
				getActionBar().hide();
			}else{
				rootView.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
				getActionBar().show();
			}
		}
    };    
}