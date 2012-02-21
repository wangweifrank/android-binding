package gueei.binding.viewAttributes.textView;

import gueei.binding.ViewAttribute;
import android.graphics.drawable.Drawable;
import android.widget.TextView;


public class CompoundDrawableViewAttribute extends ViewAttribute<TextView, Object> {

	public CompoundDrawableViewAttribute(TextView view, String type) {
		super(Object.class, view, type);
	}
	
	Drawable mValue = null;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
	
		if (newValue instanceof Integer){
			mValue = getView().getContext().getResources().getDrawable((Integer)newValue);
		} else if (newValue instanceof Drawable){
			mValue = (Drawable)newValue;
		} else {
			mValue = null;
		}

		//getCompoundDrawables - drawables for the left, top, right, and bottom borders.		
		//setCompoundDrawablesWithIntrinsicBounds (Drawable left, Drawable top, Drawable right, Drawable bottom)
		
		if(attributeName.equals("drawableLeft")) {
			getView().setCompoundDrawablesWithIntrinsicBounds(
					mValue, getView().getCompoundDrawables()[1], 
					getView().getCompoundDrawables()[2], getView().getCompoundDrawables()[3]);
		} else if(attributeName.equals("drawableTop")) {
			getView().setCompoundDrawablesWithIntrinsicBounds(
					getView().getCompoundDrawables()[0], mValue, 
					getView().getCompoundDrawables()[2], getView().getCompoundDrawables()[3]);
		} else if(attributeName.equals("drawableRight")) {
			getView().setCompoundDrawablesWithIntrinsicBounds(
					getView().getCompoundDrawables()[0], getView().getCompoundDrawables()[1], 
					mValue, getView().getCompoundDrawables()[3]);			
		} else if(attributeName.equals("drawableBottom")) {
			getView().setCompoundDrawablesWithIntrinsicBounds(
					getView().getCompoundDrawables()[0], getView().getCompoundDrawables()[1], 
					getView().getCompoundDrawables()[2], mValue);		
		}				
	}

	@Override
	public Object get() {
		return mValue;
	}
}
