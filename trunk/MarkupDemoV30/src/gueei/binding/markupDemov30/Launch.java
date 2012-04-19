package gueei.binding.markupDemov30;

import gueei.binding.markupDemov30.viewModels.LaunchViewModel;
import gueei.binding.v30.ActivityBinder;
import gueei.binding.v30.actionbar.BindableActionBar;
import gueei.binding.v30.app.BindingActivityV30;
import android.os.Bundle;

public class Launch extends BindingActivityV30 {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LaunchViewModel vm = new LaunchViewModel(this);
        this.bind(R.xml.main_metadata, vm);
        //ActionBarBinder.BindActionBar(this, R.xml.main_metadata, vm);
        BindableActionBar v = ActivityBinder.inflateActionBar(this, R.xml.main_metadata);
        ActivityBinder.BindActionBar(this, 
        		v, vm);
    }
}