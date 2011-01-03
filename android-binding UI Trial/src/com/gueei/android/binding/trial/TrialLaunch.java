package com.gueei.android.binding.trial;

import java.lang.reflect.Field;

import org.xmlpull.v1.XmlPullParser;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingActivity;
import com.gueei.android.binding.Observable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TrialLaunch extends Activity {
	private LayoutInflater inflater;
	private class Model{
		Observable<CharSequence> hello = new Observable<CharSequence>("BINDING");
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = this.getLayoutInflater();
        inflater.setFactory(this);
		mBinder = new Binder();
        setContentView(R.layout.main, new Model());
    }
    
    
    private Binder mBinder;
	private Object model;

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		Log.d("Binder", "Create View Activity: " + name);
		for(int i=0; i<attrs.getAttributeCount(); i++){
			Log.d("Binder", "View attrs: " + i + ": " + attrs.getAttributeName(i));
		}
		int id = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "id", 0);
		Log.d("Binder", "View Id: " + id);
		try{
			String prefix = "android.widget.";
			if ((name=="View") || (name=="ViewGroup")) prefix = "android.view.";
			
			View view =  inflater.createView(name, prefix, attrs);
			if (TextView.class.isInstance(view)){
				// This is actually capture the custom attribute name for binding
				String text = attrs.getAttributeValue("http://code.google.com/p/android-binding", "text");
				Field f = model.getClass().getDeclaredField(text);
				Observable<CharSequence> prop = (Observable<CharSequence>)f.get(model);
				mBinder.bind(view, "Text", 
						TextView.class.getMethod("getText"),
						TextView.class.getMethod("setText", CharSequence.class),
						prop);
			} 
			return view;
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e("Binder", e.getMessage());
			return null;
		}
		/*
		View view;
		try {
			view = inflater.createView(name, "", attrs);
		if (view.getClass().isInstance(TextView.class)){
			// This is actually capture the custom attribute name for binding
			String text = attrs.getAttributeValue("http://code.google.com/p/android-binding", "Text");
				Field f = model.getClass().getDeclaredField(text);
				Observable<CharSequence> prop = (Observable<CharSequence>)f.get(model);
				mBinder.bind(view, "Text", prop);
			} 
		return view;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}*/
		//return null;
	}

	public void setContentView(int layoutResID, Object model) {
		this.model = model;
		super.setContentView(layoutResID);
	}
}