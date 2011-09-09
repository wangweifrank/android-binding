package gueei.binding.app;

import gueei.binding.Binder;
import gueei.binding.Binder.InflateResult;
import gueei.binding.menu.OptionsMenuBinder;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class BindingActivity extends Activity {

	OptionsMenuBinder menuBinder;
	Object mMenuViewModel;
	View mRootView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected View setAndBindRootView(int layoutId, Object... contentViewModel){
		if (mRootView!=null){
			throw new IllegalStateException("Root view is already created");
		}
		InflateResult result = Binder.inflateView(this, layoutId, null, false);
		mRootView = result.rootView;
		for(int i=0; i<contentViewModel.length; i++){
			Binder.bindView(this, result, contentViewModel[i]);
		}
		setContentView(mRootView);
		return mRootView;
	}
	
	protected void setAndBindOptionsMenu(int menuId, Object menuViewModel){
		if (menuBinder!=null){
			throw new IllegalStateException("Options menu can only set once");
		}
		menuBinder = new OptionsMenuBinder(menuId);
		mMenuViewModel = menuViewModel;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// No menu is defined
		if (menuBinder==null)
			return false;
		return menuBinder.onCreateOptionsMenu(this, menu, mMenuViewModel);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (menuBinder==null)
			return false;
		return menuBinder.onPrepareOptionsMenu(this, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		return menuBinder.onOptionsItemSelected(this, item);
	}
}
