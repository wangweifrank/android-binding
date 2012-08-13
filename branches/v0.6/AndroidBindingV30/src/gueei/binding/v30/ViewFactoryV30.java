package gueei.binding.v30;

import java.lang.ref.WeakReference;

import gueei.binding.Binder;
import gueei.binding.BindingMap;
import gueei.binding.ViewFactory;
import gueei.binding.v30.app.BindingFragment;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class ViewFactoryV30 extends ViewFactory implements LayoutInflater.Factory2 {

	private WeakReference<Activity> activityRef;
	
	public ViewFactoryV30(LayoutInflater inflater) {
		super(inflater);
		Context context = inflater.getContext();
		if (context instanceof Activity){
			activityRef = new WeakReference<Activity>((Activity)context);
		}
	}

	public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
		View view = CreateViewByInflater(parent, name, context, attrs);
		if (view==null) return null;
		createBindingMapForView(view, attrs);
		return view;
	}

	protected View CreateViewByInflater(View parent, String name, Context context,
			AttributeSet attrs) {
		if (name.equals("fragment")){
			// Context of this inflater is not Activity,
			// Cannot create fragment
			if (activityRef==null || activityRef.get() == null){
				return null;
			}

			Fragment fragment = Fragment.instantiate(activityRef.get(), 
					attrs.getAttributeValue(null, "class"), activityRef.get().getIntent().getExtras());
			if (!(fragment instanceof BindingFragment)){
				return null;
			}
			
			BindingMap map = new BindingMap();
			int count = attrs.getAttributeCount();
			for(int i=0; i<count; i++){
				String attrName = attrs.getAttributeName(i);
				String attrValue = attrs.getAttributeValue(Binder.BINDING_NAMESPACE, attrName);
				if (attrValue!=null){
					map.put(attrName, attrValue);
				}
			}
			
			((BindingFragment)fragment).setBindingMap(map);
			
			activityRef.get().getFragmentManager()
				.beginTransaction()
				.add(0, fragment)
				.addToBackStack(null)
				.commit();
			
			return new FragmentViewStub(context, (BindingFragment)fragment);
		}
		
		return super.CreateViewByInflater(name, context, attrs);
	}
	
	/**
	 * FragmentViewStub is used for inplace of the Fragment, before the Fragment is actually
	 * created and inflated. 
	 * The replacing logic is copied from the original ViewStub.inflate() method
	 * @author andy
	 *
	 */
	private class FragmentViewStub extends View implements BindingFragment.FragmentEventListener{
		BindingFragment mFragment;
		public FragmentViewStub(Context context, BindingFragment fragment) {
			super(context);
			mFragment = fragment;
			mFragment.setFragmentEventListener(this);
		}
		
		public void onViewCreated(View view, Bundle savedInstanceState) {
			final ViewParent viewParent = getParent();
			if (viewParent != null && viewParent instanceof ViewGroup) {
				final ViewGroup parent = (ViewGroup) viewParent;
				final int index = parent.indexOfChild(this);
                parent.removeViewInLayout(this);

                final ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (layoutParams != null) {
                    parent.addView(view, index, layoutParams);
                } else {
                    parent.addView(view, index);
                }
			}else {
	            throw new IllegalStateException("Fragment must have a non-null ViewGroup viewParent");
	        }
		}
	}
}
