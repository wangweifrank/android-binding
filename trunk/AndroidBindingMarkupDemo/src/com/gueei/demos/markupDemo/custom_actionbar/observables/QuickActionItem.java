package com.gueei.demos.markupDemo.custom_actionbar.observables;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import gueei.binding.Observable;

/**
 * User: =ra=
 * Date: 13.08.11
 * Time: 17:50
 */
public class QuickActionItem extends Observable<QuickActionItem> {

	private Drawable     mIcon;
	private Bitmap       mThumbBitmap;
	private CharSequence mText;
	private boolean      mSelected;

	public QuickActionItem(CharSequence text, Drawable icon, boolean selected, Bitmap thumbBitmap) {
		super(QuickActionItem.class);
		mText = text;
		mSelected = selected;
		mIcon = icon;
		mThumbBitmap = thumbBitmap;
	}

	public QuickActionItem(CharSequence text, Drawable icon, boolean selected) {
		this(text, icon, selected, null);
	}

	public QuickActionItem(CharSequence text, Drawable icon) {
		this(text, icon, false, null);
	}

	/**
	 * Get action text
	 *
	 * @return action text {@link java.lang.CharSequence}
	 */
	public CharSequence getText() {
		return mText;
	}

	/**
	 * Set action text
	 *
	 * @param text {@link java.lang.CharSequence} action text
	 */
	public void setText(CharSequence text) {
		mText = text;
		notifyChanged(this);
	}

	/**
	 * Get action icon
	 *
	 * @return icon {@link android.graphics.drawable.Drawable} action icon
	 */
	public Drawable getIcon() {
		return mIcon;
	}

	/**
	 * Set action icon
	 *
	 * @param icon {@link android.graphics.drawable.Drawable} action icon
	 */
	public void setIcon(Drawable icon) {
		mIcon = icon;
		notifyChanged(this);
	}

	/**
	 * Check if item is selected
	 *
	 * @return true or false
	 */
	public boolean isSelected() {
		return mSelected;
	}

	/**
	 * Set selected flag
	 *
	 * @param selected Flag to indicate the item is selected
	 */
	public void setSelected(boolean selected) {
		mSelected = selected;
		notifyChanged(this);
	}

	/**
	 * Get thumb image
	 *
	 * @return thumb {@link android.graphics.Bitmap} action image
	 */
	public Bitmap getThumbBitmap() {
		return mThumbBitmap;
	}

	/**
	 * Set thumb
	 *
	 * @param thumbBitmap {@link android.graphics.Bitmap} Thumb image
	 */
	public void setThumb(Bitmap thumbBitmap) {
		mThumbBitmap = thumbBitmap;
	}
}