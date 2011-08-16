package com.gueei.demos.markupDemo.custom_actionbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import com.gueei.demos.markupDemo.R;
import gueei.binding.Binder;

/**
 * User: =ra=
 * Date: 16.08.11
 * Time: 13:28
 */
public class ActCustomAB extends Activity {

	CustomAB mModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mModel = new CustomAB(this);
		Binder.setAndBindContentView(this, R.layout.custom_ab_act_main, mModel);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return mModel.onCreateOptionsMenu(menu);
	}
}
