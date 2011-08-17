package gueei.binding.listeners;

import android.os.Handler;
import android.view.View;
import android.widget.TabHost;
import gueei.binding.viewAttributes.tabHost.OnTabChangedViewEvent;
import gueei.binding.viewAttributes.tabHost.TabSelectedPositionViewAttribute;

/**
 * User: =ra=
 * Date: 17.08.11
 * Time: 13:32
 */
public class OnTabChangedListener extends ViewMulticastListener<TabHost.OnTabChangeListener> implements TabHost.OnTabChangeListener {

	private Handler mHandler = new Handler();

	private class ListenerRunnable implements Runnable {

		private final TabHost.OnTabChangeListener mListener;
		private final String                      mTabId;

		private ListenerRunnable(TabHost.OnTabChangeListener listener, String tabId) {
			mListener = listener;
			mTabId = tabId;
		}

		public void run() {
			mListener.onTabChanged(mTabId);
		}
	}

	public void onTabChanged(String tabId) {
			for (TabHost.OnTabChangeListener l : listeners) {
				if (l instanceof TabSelectedPositionViewAttribute) {
					mHandler.post(new ListenerRunnable(l, tabId));
				}
			}
			for (TabHost.OnTabChangeListener l : listeners) {
				if (l instanceof OnTabChangedViewEvent) {
					mHandler.post(new ListenerRunnable(l, tabId));
				}
			}
	}

	@Override public void registerToView(View tabHost) {
		if (!(tabHost instanceof TabHost)) {
			return;
		}
		((TabHost) tabHost).setOnTabChangedListener(this);
	}
}