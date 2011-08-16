package com.gueei.demos.markupDemo.custom_actionbar.observables;

import gueei.binding.Observable;

/**
 * User: =ra=
 * Date: 13.08.11
 * Time: 17:47
 */
public class NotificationItemDataModel extends Observable<NotificationItemDataModel> {

	private final int          mId;
	private final CharSequence mText;
	private final int          mDrawableId;
	private       boolean      mIsChecked;

	public NotificationItemDataModel(int id, CharSequence itemText, boolean checked, int drawableId) {
		super(NotificationItemDataModel.class);
		mId = id;
		mText = itemText;
		mIsChecked = checked;
		mDrawableId = drawableId;
	}

	public int getId() {
		return mId;
	}

	public CharSequence getText() {
		return mText;
	}

	public boolean isChecked() {
		return mIsChecked;
	}

	public void setChecked(boolean checked) {
		if (mIsChecked != checked) {
			mIsChecked = checked;
			notifyChanged(this);
		}
	}

	public int getDrawableId() {
		return mDrawableId;
	}
}