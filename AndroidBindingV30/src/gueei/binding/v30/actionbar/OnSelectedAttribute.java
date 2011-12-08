package gueei.binding.v30.actionbar;

import gueei.binding.Command;
import gueei.binding.EventAttribute;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;

public class OnSelectedAttribute extends EventAttribute<Tab> 
	implements TabListener{
	
	public OnSelectedAttribute(Tab view) {
		super(view, "onSelected");
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Command cmd = this.get();
		if (cmd!=null)
			cmd.Invoke(null, tab, ft);
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	protected void registerToListener(Tab host) {
		host.setTabListener(this);
	}
}
