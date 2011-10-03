package gueei.binding.v30.actionbar;

import gueei.binding.MulticastListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;

public class TabListenerMulticast extends MulticastListener<Tab, TabListener> implements
		TabListener {

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		for(TabListener l: listeners){
			l.onTabReselected(tab, ft);
		}
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		for(TabListener l: listeners){
			l.onTabSelected(tab, ft);
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		for(TabListener l: listeners){
			l.onTabUnselected(tab, ft);
		}
	}

	@Override
	public void registerToHost(Tab host) {
		host.setTabListener(this);
	}
}
