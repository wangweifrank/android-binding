package com.gueei.demos.markupDemo;

import android.app.TabActivity;
import android.os.Bundle;
import com.gueei.demos.markupDemo.viewModels.TabDemo;

/**
 * User: =ra=
 * Date: 16.08.11
 * Time: 15:54
 */
public class TabDemoActivity extends TabActivity{

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gueei.binding.Binder.setAndBindContentView(this, R.layout.tabdemo, new TabDemo(this));
	}
}
