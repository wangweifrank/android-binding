package com.gueei.android.binding;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class BindingActivity extends Activity {
	private Binder mBinder;
	private Object model;
	private LayoutInflater.Factory mDefaultFactory;

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		Log.d("Binder", "Create View Activity" + name);
		View view = mDefaultFactory.onCreateView(name, context, attrs);
		if (view.getClass().isInstance(TextView.class)){
			// This is actually capture the custom attribute name for binding
			String text = attrs.getAttributeValue("http://code.google.com/p/android-binding", "Text");
			try {
				Field f = model.getClass().getDeclaredField(text);
				Observable<CharSequence> prop = (Observable<CharSequence>)f.get(model);
				mBinder.bind(view, "Text", prop);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return view;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDefaultFactory = this.getLayoutInflater().getFactory();
		this.getLayoutInflater().setFactory(this);
		mBinder = new Binder();
	}

	public void setContentView(int layoutResID, Object model) {
		super.setContentView(layoutResID);
		this.model = model;
	}	
}
