package com.gueei.demo.abgallery;

import gueei.binding.Command;
import gueei.binding.DependentObservable;
import gueei.binding.Observable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.v30.actionbar.ActionBarBinder;
import gueei.binding.v30.app.BindingActivityV30;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class CopyOfMainActivity extends BindingActivityV30 {
	// Work around for setting system ui visibility... 
	// no idea why this is implemented in View rather than other topper level stuff... 
	private View rootView;

	private Animator mCurrentTitlesAnimator;
	
	private int mThemeId = 0; 
	
	public void toggleVisibleTitles() {
        // Use these for custom animations.
        final FragmentManager fm = getFragmentManager();
        final TitlesFragment f = (TitlesFragment) fm
                .findFragmentById(R.id.frag_title);
        final View titlesView = f.getView();

        // Determine if we're in portrait, and whether we're showing or hiding the titles
        // with this toggle.
        final boolean isPortrait = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT;

        final boolean shouldShow = f.isHidden() || mCurrentTitlesAnimator != null;
        
        TitleVisible.set(shouldShow);

        // Cancel the current titles animation if there is one.
        if (mCurrentTitlesAnimator != null)
            mCurrentTitlesAnimator.cancel();

        // Begin setting up the object animator. We'll animate the bottom or right edge of the
        // titles view, as well as its alpha for a fade effect.
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                titlesView,
                PropertyValuesHolder.ofInt(
                        isPortrait ? "bottom" : "right",
                        shouldShow ? getResources().getDimensionPixelSize(R.dimen.titles_size)
                                   : 0),
                PropertyValuesHolder.ofFloat("alpha", shouldShow ? 1 : 0)
        );

        // At each step of the animation, we'll perform layout by calling setLayoutParams.
        final ViewGroup.LayoutParams lp = titlesView.getLayoutParams();
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // *** WARNING ***: triggering layout at each animation frame highly impacts
                // performance so you should only do this for simple layouts. More complicated
                // layouts can be better served with individual animations on child views to
                // avoid the performance penalty of layout.
                if (isPortrait) {
                    lp.height = (Integer) valueAnimator.getAnimatedValue();
                } else {
                    lp.width = (Integer) valueAnimator.getAnimatedValue();
                }
                titlesView.setLayoutParams(lp);
            }
        });

        if (shouldShow) {
            fm.beginTransaction().show(f).commit();
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    mCurrentTitlesAnimator = null;
                }
            });

        } else {
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                boolean canceled;

                @Override
                public void onAnimationCancel(Animator animation) {
                    canceled = true;
                    super.onAnimationCancel(animation);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (canceled)
                        return;
                    mCurrentTitlesAnimator = null;
                    fm.beginTransaction().hide(f).commit();
                }
            });
        }

        // Start the animation.
        objectAnimator.start();
        mCurrentTitlesAnimator = objectAnimator;

        invalidateOptionsMenu();

        // Manually trigger onNewIntent to check for ACTION_DIALOG.
        onNewIntent(getIntent());
    }

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
			toggleVisibleTitles();
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