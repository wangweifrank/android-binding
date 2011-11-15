package com.gueei.demo.abgallery;

import gueei.binding.app.BindingActivity;
import gueei.binding.v30.app.BindingActivityV30;
import android.os.Bundle;

import com.gueei.demo.abgallery.viewModels.ContentVM;

public class ContentActivity extends BindingActivityV30 {
	ContentVM vm;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        vm = new ContentVM();
        vm.ResId.set(getIntent().getIntExtra("ID", 0));
        
        this.setAndBindRootView(R.layout.content_welcome, vm);

        //this.setAndBindOptionsMenu(R.menu.main_menu, vm);
        //ActionBarBinder.BindActionBar(this, R.xml.main_metadata, vm);
    }
}
