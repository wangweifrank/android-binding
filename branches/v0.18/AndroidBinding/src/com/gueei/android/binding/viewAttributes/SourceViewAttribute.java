package com.gueei.android.binding.viewAttributes;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.gueei.android.binding.BindingType;
import com.gueei.android.binding.ViewAttribute;

public class SourceViewAttribute extends ViewAttribute<ImageView, Object> {

	public SourceViewAttribute(ImageView view) {
		super(Object.class, view, "source");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue==null){
			getView().setImageDrawable(null);
			return;
		}
		if (newValue instanceof Integer){
			getView().setImageResource((Integer)newValue);
		}
		if (newValue instanceof Uri){
			getView().setImageURI((Uri)newValue);
			return;
		}
		if (newValue instanceof Drawable){
			getView().setImageDrawable((Drawable)newValue);
			return;
		}
		if (newValue instanceof Bitmap){
			getView().setImageBitmap((Bitmap)newValue);
			return;
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}

	@Override
	public Object get() {
		return null;
	}
}
