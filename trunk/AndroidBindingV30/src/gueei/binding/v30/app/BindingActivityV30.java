package gueei.binding.v30.app;

import gueei.binding.Binder.InflateResult;
import gueei.binding.app.BindingActivity;
import gueei.binding.v30.BinderV30;
import gueei.binding.v30.actionbar.ActionBarBinder;
import android.view.View;

public class BindingActivityV30 extends BindingActivity {

	@Override
	protected View setAndBindRootView(int layoutId, Object... contentViewModel) {
		if (getRootView()!=null){
			throw new IllegalStateException("Root view is already created");
		}
		InflateResult result = BinderV30.inflateView(this, layoutId, null, false);
		setRootView(result.rootView);
		for(int i=0; i<contentViewModel.length; i++){
			BinderV30.bindView(this, result, contentViewModel[i]);
		}
		setContentView(getRootView());
		return getRootView();
	}
	
	protected void bindActionBar(int xmlId, Object model){
		ActionBarBinder.BindActionBar(this, xmlId, model);
	}
}
