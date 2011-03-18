package com.gueei.demos.markupDemo.viewModels;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.gueei.android.binding.Observable;
import com.gueei.android.binding.observables.IntegerObservable;
import com.gueei.demos.markupDemo.R;

public class ImageView {
	public IntegerObservable ImageFromResourceId = new IntegerObservable(R.drawable.icon);
	public Observable<Drawable> ImageFromDrawable = new Observable<Drawable>(Drawable.class);
	
	public ImageView(Activity activity){
		ImageFromDrawable.set(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
				new int[]{Color.argb(0, 0, 0, 0), Color.rgb(220, 100, 100), Color.argb(0, 0, 0, 0)}));
	}
}
