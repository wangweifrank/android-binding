package gueei.binding.markupDemov30;

import gueei.binding.markupDemov30.viewModels.LaunchViewModel;
import gueei.binding.v30.actionbar.ActionBarBinder;
import gueei.binding.v30.app.BindingActivityV30;
import android.os.Bundle;

public class Launch extends BindingActivityV30 {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Object vm = new LaunchViewModel(this);
        this.setAndBindRootView(R.layout.main, vm);
        ActionBarBinder.BindActionBar(this, R.xml.main_metadata, vm);
    }
}