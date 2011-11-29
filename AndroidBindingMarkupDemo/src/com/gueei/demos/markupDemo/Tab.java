package com.gueei.demos.markupDemo;

import gueei.binding.app.BindingTabActivity;
import android.os.Bundle;

public class Tab extends BindingTabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 this.setAndBindRootView(R.layout.tab, this);
	}

}
