package com.gueei.demo.abgallery;

import java.util.Collection;

import gueei.binding.Binder.InflateResult;
import gueei.binding.Command;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.v30.BinderV30;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ContentFragment extends Fragment {
	private ActionMode mActionMode;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Activity activity = getActivity();
		InflateResult result = BinderV30.inflateView(activity, R.layout.content_welcome, container, false);
		
		if (activity instanceof MainActivity){
			((MainActivity)activity).DisplayingImage.subscribe(new Observer(){
				public void onPropertyChanged(IObservable<?> prop,
						Collection<Object> initiators) {
					if (mActionMode!=null){
						mActionMode.finish();
					}
					InSelectionMode.set(false);
				}
			});
		}
		
		// Multiple binding
		BinderV30.bindView(activity, result, activity);
		BinderV30.bindView(activity, result, this);
		
		return result.rootView;
	}
	
	public final Command ToggleSelectionMode = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			InSelectionMode.set(true);
			mActionMode = getActivity().startActionMode(mContentSelectionActionModeCallback);
		}
	};
	
	public final BooleanObservable InSelectionMode = new BooleanObservable(false);
	
	/*
	 * ActionMode is not supported by Android Binding now, 
	 * further study on this would be necessary before implementing
	 */
	private ActionMode.Callback mContentSelectionActionModeCallback = new ActionMode.Callback() {
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.setTitle(R.string.photo_selection_cab_title);

            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.photo_context_menu, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.share:
                    //shareCurrentPhoto();
                    actionMode.finish();
                    return true;
            }
            return false;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            //mContentView.setSelected(false);
            mActionMode = null;
            InSelectionMode.set(false);
        }
    };
}
