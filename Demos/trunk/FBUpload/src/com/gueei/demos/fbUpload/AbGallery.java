package com.gueei.demos.fbUpload;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;

public class AbGallery extends Gallery{

	public AbGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AbGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AbGallery(Context context) {
		super(context);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		super.onLongPress(e);
		Log.d("Binder", "AbGallery long press");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("Binder", "AbGallery key down");
		//super.onKeyDown(keyCode, event);
		return false;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.d("Binder", "dispatch: " + super.dispatchKeyEvent(event));
		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d("Binder", "AbGallery key down");
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.d("Binder", "AbGallery motion down");
		return true;
	}

}