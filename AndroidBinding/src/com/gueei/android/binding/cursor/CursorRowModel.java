package com.gueei.android.binding.cursor;

import android.content.Context;

public abstract class CursorRowModel {
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
