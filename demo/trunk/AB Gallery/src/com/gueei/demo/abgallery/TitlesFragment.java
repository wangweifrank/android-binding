package com.gueei.demo.abgallery;

import gueei.binding.v30.BinderV30;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TitlesFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Activity activity = getActivity();
		
		// View Model coming from the activity
		return BinderV30.createAndBindView(activity, R.layout.title, container, activity);
	}
}
