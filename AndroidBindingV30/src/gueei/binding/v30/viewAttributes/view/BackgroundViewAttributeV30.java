package gueei.binding.v30.viewAttributes.view;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;

public class BackgroundViewAttributeV30  extends ViewAttribute<View, Object> {
	
	public BackgroundViewAttributeV30(View view) {
		super(Object.class, view, "backgroundv30");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null){
			if(getView().getBackground() instanceof AnimationDrawable) {
				AnimationDrawable frameAnimation = (AnimationDrawable)getView().getBackground();
				if(frameAnimation.isRunning())
					frameAnimation.stop();
			}
			getView().setBackgroundDrawable(null);
			return;
		}
		if (newValue instanceof Integer){
			getView().setBackgroundResource((Integer)newValue);
		}
		if (newValue instanceof AnimationDrawable){
			getView().setBackgroundDrawable((AnimationDrawable)newValue);			
			AnimationDrawable frameAnimation = (AnimationDrawable)newValue;
			frameAnimation.start();			
			return;
		}
		if (newValue instanceof Drawable){
			getView().setBackgroundDrawable((Drawable)newValue);
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
