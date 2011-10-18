package gueei.binding.v30.app;

import gueei.binding.Binder.InflateResult;
import gueei.binding.BindingMap;
import gueei.binding.v30.BinderV30;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

/**
 * Fragment can add custom binding:tag only if it is a sub-class of BindingFragment
 * 
 * @author andy
 *
 */
public abstract class BindingFragment extends Fragment {

	private BindingMap mBindingMap;
	
	public interface FragmentEventListener{
		public void onViewCreated(View view, Bundle savedInstanceState);
	}
	
	private FragmentEventListener mFragmentEventListener;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if(mFragmentEventListener!=null){
			mFragmentEventListener.onViewCreated(view, savedInstanceState);
		}
		super.onViewCreated(view, savedInstanceState);
	}

	public FragmentEventListener getFragmentEventListener() {
		return mFragmentEventListener;
	}

	public void setFragmentEventListener(FragmentEventListener fragmentEventListener) {
		mFragmentEventListener = fragmentEventListener;
	}

	/**
	 * The View will be inflated with attached Binding Map
	 * @param layoutId
	 * @return
	 */
	protected InflateResult inflateView(int layoutId){
		return inflateView(true, layoutId);
	}
	
	protected InflateResult inflateView(boolean passAttributes, int layoutId){
		InflateResult result = BinderV30.inflateView(getActivity(), layoutId, null, false);
		if(passAttributes){
			if (mBindingMap!=null)
				BinderV30.mergeBindingMap(result.rootView, mBindingMap);
		}
		
		return result;
	}
	
	protected void bindView(InflateResult result, Object... viewModels){
		for(int i=0; i<viewModels.length; i++){
			BinderV30.bindView(getActivity(), result, viewModels[i]);
		}
	}

	public BindingMap getBindingMap() {
		return mBindingMap;
	}

	public void setBindingMap(BindingMap bindingMap) {
		mBindingMap = bindingMap;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		onBind();
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * This is executed once the activity is created
	 * to help deferred binding, since the Fragment's view is supposed to be created before Activity
	 */
	protected abstract void onBind();
}
