package gueei.binding.v30.viewAttributes;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.res.Resources;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import gueei.binding.BindingType;
import gueei.binding.DynamicObject;
import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.view.AnimationTrigger;
import gueei.binding.viewAttributes.view.AnimationTrigger.TriggerListener;
import gueei.binding.viewAttributes.view.ConditionalAnimationTrigger;

public class AnimationViewAttributeV30 extends ViewAttribute<View, AnimationTrigger> 
	implements TriggerListener {

	public AnimationViewAttributeV30(View view) {
		super(AnimationTrigger.class, view, "animation");
	}
	
	private AnimationTrigger mValue;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (mValue!=null)
			mValue.removeTriggerListener(this);
		if (newValue instanceof DynamicObject){
			mValue = ConditionalAnimationTrigger.createFromDynamicObject((DynamicObject)newValue);
			mValue.setTriggerListener(this);
			return;
		}
		if (newValue instanceof AnimationTrigger){
			mValue = (AnimationTrigger)newValue;
			mValue.setTriggerListener(this);
			return;
		}
	}

	@Override
	public AnimationTrigger get() {
		return mValue;
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (AnimationTrigger.class.isAssignableFrom(type))
			return BindingType.OneWay;
		if (AnimationTrigger.class.equals(type))
			return BindingType.OneWay;
		if (DynamicObject.class.isAssignableFrom(type))
			return BindingType.OneWay;
		return BindingType.NoBinding;
	}

	public void fireAnimation(AnimationTrigger trigger) {
		try{
			Animator anim = AnimatorInflater.loadAnimator(getView().getContext(), mValue.getAnimationId());
			anim.setTarget(getView());
			anim.start();
		}catch(Resources.NotFoundException e){
			return;
		}
	}
}
