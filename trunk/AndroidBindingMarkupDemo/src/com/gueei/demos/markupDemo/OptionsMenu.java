package com.gueei.demos.markupDemo;

import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import gueei.binding.observables.BooleanObservable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/*
 * BindingActivity is a shorthand class to help all sort of binding to 
 * 1. Option menu
 * 2. Context menu (coming soon)
 * 3. HoneyComb ActionBar
 * If you need more control on binding, see the next comment block
 */
public class OptionsMenu extends BindingActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setAndBindRootView(R.layout.optionsmenu, this);
		this.setAndBindOptionsMenu(R.menu.optionsmenu, this);
	}
	
	public final BooleanObservable BrowserVisible = new BooleanObservable(true);
	public final Command ToggleBrowser = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			BrowserVisible.toggle();
		}
	};
	
	public final Command SubMenuClick = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			Toast
				.makeText(OptionsMenu.this, "SubMenu Clicked: " + args[0].toString(), Toast.LENGTH_SHORT)
				.show();
		}
	};
}

/* This is the Raw mechanism for binding to Menu.. 
 * It requires the activity to pass the relevant events to menu binder
public class OptionsMenu extends Activity {
	MenuBinder menuBinder;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    menuBinder = new MenuBinder(R.menu.menu);
	}

	// Redirect the activity events to menu binder
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return menuBinder.onCreateOptionsMenu(this, menu, this);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return menuBinder.onPrepareOptionsMenu(this, menu);
	}
}
*/