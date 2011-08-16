package com.gueei.demos.markupDemo.custom_actionbar;

import android.R;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.gueei.demos.markupDemo.custom_actionbar.collections.ObservableListObservable;
import com.gueei.demos.markupDemo.custom_actionbar.observables.NotificationItemDataModel;
import com.gueei.demos.markupDemo.custom_actionbar.observables.QuickActionItem;
import com.gueei.demos.markupDemo.custom_actionbar.observables.QuickActionObservable;
import com.gueei.demos.markupDemo.custom_actionbar.widgets.QuickAction;
import gueei.binding.Command;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

import java.util.Random;

/**
 * User: =ra=
 * Date: 16.08.11
 * Time: 13:13
 */
public class CustomAB {

	public       StringObservable                                    Caption             = new StringObservable("Binded Caption");
	public       IntegerObservable                                   Visibility          = new IntegerObservable(View.VISIBLE);
	public       IntegerObservable                                   ProgressState       = new IntegerObservable(View.VISIBLE);
	public       IntegerObservable                                   CommandFirerIdx     = new IntegerObservable(-1);
	public       ObservableListObservable<NotificationItemDataModel> SourceNotifications =
			new ObservableListObservable<NotificationItemDataModel>(NotificationItemDataModel.class);
	public final QuickActionObservable                               TopQuickAction      = new QuickActionObservable();
	public final Command                                             QActionBtnClicked   = new Command() {
		public void Invoke(View view, Object... args) {
			if (ProgressState.get() == View.VISIBLE) {
				ProgressState.set(View.GONE);
			}
			else {
				ProgressState.set(View.VISIBLE);
			}
			Toast.makeText(mContext, "Top right corner button clicked", Toast.LENGTH_SHORT).show();
		}
	};
	public final Command                                             TopActionClicked    = new Command() {
		public void Invoke(View view, Object... args) {
			Toast.makeText(mContext, SourceNotifications.getItem(CommandFirerIdx.get()).getText() + " clicked", Toast.LENGTH_SHORT).show();
		}
	};
	//
	private      Activity                                            mContext            = null;
	private      QuickAction                                         mQuickActionTop     = null;
	private      QuickAction                                         mQuickActionBottom  = null;

	public CustomAB(Activity context) {
		mContext = context;
		setupQuickActionTop();
		setupQuickActionBottom();
		populateSourceNotifications();
	}

	@SuppressWarnings({"UnusedParameters"})
	protected boolean onCreateOptionsMenu(Menu menu) {
		if (null != mQuickActionBottom) {
			mQuickActionBottom.showBottom();
		}
		return false;
	}

	protected void setupQuickActionTop() {
		mQuickActionTop = new QuickAction(mContext);
		mQuickActionTop.initActionsList(SourceNotifications);
		TopQuickAction.set(mQuickActionTop);
	}

	protected void setupQuickActionBottom() {
		mQuickActionBottom = new QuickAction(mContext);
		initQuickActionsBottom();
	}

	private void populateSourceNotifications() {
		Random random = new Random();
		SourceNotifications.add(new NotificationItemDataModel(random.nextInt(), "Notification 1 ", true, R.drawable.stat_sys_vp_phone_call_on_hold));
		SourceNotifications.add(new NotificationItemDataModel(random.nextInt(), "Notification 2 ", false, R.drawable.stat_sys_warning));
		SourceNotifications.add(new NotificationItemDataModel(random.nextInt(), "Notification 3 ", false, R.drawable.stat_sys_vp_phone_call));
		SourceNotifications.add(new NotificationItemDataModel(random.nextInt(), "Notification 4 ", false, R.drawable.stat_sys_vp_phone_call_on_hold));
		SourceNotifications.add(new NotificationItemDataModel(random.nextInt(), "Notification 5 ", true, R.drawable.stat_sys_phone_call));
		SourceNotifications.add(new NotificationItemDataModel(random.nextInt(), "Notification 6 ", false, R.drawable.stat_sys_phone_call_forward));
		SourceNotifications.add(new NotificationItemDataModel(random.nextInt(), "Notification 7 ", true, R.drawable.stat_notify_missed_call));
	}

	private void initQuickActionsBottom() {
		QuickActionItem menuAction1 = new QuickActionItem("Menu 1", mContext.getResources().getDrawable(R.drawable.ic_menu_myplaces));
		QuickActionItem menuAction2 = new QuickActionItem("Menu 2", mContext.getResources().getDrawable(R.drawable.ic_menu_mylocation));
		QuickActionItem menuAction3 = new QuickActionItem("Menu 3", mContext.getResources().getDrawable(R.drawable.ic_menu_compass));
		QuickActionItem menuAction4 = new QuickActionItem("Menu 4", mContext.getResources().getDrawable(R.drawable.ic_menu_camera));
		QuickActionItem menuAction5 = new QuickActionItem("Menu 5", mContext.getResources().getDrawable(R.drawable.ic_menu_directions));
		mQuickActionBottom.addActionItem(menuAction1);
		mQuickActionBottom.addActionItem(menuAction2);
		mQuickActionBottom.addActionItem(menuAction3);
		mQuickActionBottom.addActionItem(menuAction4);
		mQuickActionBottom.addActionItem(menuAction5);
		//
		mQuickActionBottom.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(final QuickActionItem action) {
				Toast.makeText(mContext, action.getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
