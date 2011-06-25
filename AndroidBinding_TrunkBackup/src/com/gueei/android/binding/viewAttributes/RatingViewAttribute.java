package com.gueei.android.binding.viewAttributes;


import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.OnRatingBarChangeListenerMulticast;

public class RatingViewAttribute extends ViewAttribute<RatingBar, Float>
	implements OnRatingBarChangeListener{

	public RatingViewAttribute(RatingBar view) {
		super(Float.class, view, "rating");
		Binder.getMulticastListenerForView(view, OnRatingBarChangeListenerMulticast.class)
			.register(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null){
			getView().setRating(0);
			return;
		}
		if (newValue instanceof Float){
			getView().setRating((Float)newValue);
			return;
		}
	}

	@Override
	public Float get() {
		return getView().getRating();
	}

	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		if (fromUser) this.notifyChanged();
	}
}