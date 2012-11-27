package gueei.binding.viewAttributes.view;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;

public class BackgroundTransitionDurationViewAttribute extends ViewAttribute<View, Object> {

	public BackgroundTransitionDurationViewAttribute(View view) {
		super(Object.class, view, "backgroundTransitionDuration");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null) return;		
		if (newValue instanceof Integer){
			if(getView().getBackground() instanceof TransitionDrawable) {
				TransitionDrawable td = (TransitionDrawable)getView().getBackground();
				td.startTransition((Integer)newValue);
			}
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
