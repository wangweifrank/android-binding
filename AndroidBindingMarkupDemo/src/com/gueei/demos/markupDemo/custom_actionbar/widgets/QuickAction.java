package com.gueei.demos.markupDemo.custom_actionbar.widgets;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gueei.demos.markupDemo.R;
import com.gueei.demos.markupDemo.custom_actionbar.observables.NotificationItemDataModel;
import com.gueei.demos.markupDemo.custom_actionbar.observables.QuickActionItem;
  import com.gueei.demos.markupDemo.custom_actionbar.collections.ObservableListObservable;

import java.util.List;

/**
 * User: =ra=
 * Date: 13.08.11
 * Time: 23:34
 */
public class QuickAction extends QuickActionPopup {

	private Context                       mContext;
	private ViewGroup                     mTrack;
	private OnActionItemClickListener     mActionItemListener;
	private OnActionListItemClickListener mActionListItemListener;
	private ListView mActionsList;

	public QuickAction(Context context) {
		super(context);
		mContext = context;
		mRootView = LayoutInflater.from(mContext).inflate(R.layout.custom_ab_widget_quickactionbar, null);
		mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);
		mActionsList = (ListView) mRootView.findViewById(R.id.actions_list);
	}

	public void addActionItem(final QuickActionItem action) {
		CharSequence title = action.getText();
		Drawable icon = action.getIcon();
		View container = LayoutInflater.from(mContext).inflate(R.layout.custom_ab_widget_quickaction_item, null);
		ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
		TextView text = (TextView) container.findViewById(R.id.tv_title);
		if (icon != null) {
			img.setImageDrawable(icon);
		}
		else {
			img.setVisibility(View.GONE);
		}
		if (title != null) {
			text.setText(title);
		}
		else {
			text.setVisibility(View.GONE);
		}
		container.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				if (mActionItemListener != null) {
					mActionItemListener.onItemClick(action);
				}
				dismiss();
			}
		});
		container.setFocusable(true);
		container.setClickable(true);
		mTrack.addView(container);
	}

	public void initActionsList(ObservableListObservable<NotificationItemDataModel> notificationsList) {
		mActionsList.setAdapter(new NotificationListAdapter(notificationsList));
	}

	public void setOnActionItemClickListener(OnActionItemClickListener listener) {
		mActionItemListener = listener;
	}

	public void setOnActionListItemClickListener(OnActionListItemClickListener listener) {
		mActionListItemListener = listener;
	}

	public void show(View anchor) {
		preShow();
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());
		mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
		int rootWidth = mRootView.getMeasuredWidth();
		int rootHeight = mRootView.getMeasuredHeight();
		//--
		int xPos = (screenWidth - rootWidth) / 2;
		int yPos = anchorRect.top - rootHeight;
		boolean onTop = true;
		// display on bottom
		if (rootHeight > anchor.getTop()) {
			yPos = anchorRect.bottom;
			onTop = false;
		}
		if (rootHeight > screenHeight - anchorRect.bottom) {
			mWindow.setHeight((screenHeight - anchorRect.bottom));
		}
		mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu : R.style.Animations_PopDownMenu);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	public void showBottom() {
		preShow();
		mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
		//--
		mWindow.setAnimationStyle(R.style.Animations_PopUpMenu);
		mWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY, screenWidth, screenHeight);
	}

	public interface OnActionItemClickListener {

		public abstract void onItemClick(QuickActionItem action);
	}

	public interface OnActionListItemClickListener {

		public abstract void onListItemClick(NotificationItemDataModel item);
	}

	private class NotificationListAdapter extends ArrayAdapter<NotificationItemDataModel> {

		private List<NotificationItemDataModel> mList;

		public NotificationListAdapter(List<NotificationItemDataModel> list) {
			super(mContext, R.layout.custom_ab_item_notifications_list, list);
			mList = list;
		}

		@Override public int getCount() {
			return mList.size();
		}

		@Override public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_ab_item_notifications_list, null);
			}
			//--
			final NotificationItemDataModel item = mList.get(position);
			final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
			checkBox.setChecked(item.isChecked());
			checkBox.setText(item.getText());
			checkBox.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					if (mActionListItemListener != null) {
						item.setChecked(checkBox.isChecked());
						mActionListItemListener.onListItemClick(item);
					}
				}
			});
			((ImageView) convertView.findViewById(R.id.image_view)).setImageResource(item.getDrawableId());
			//--
			convertView.setTag(item);
			return convertView;
		}
	}
}
