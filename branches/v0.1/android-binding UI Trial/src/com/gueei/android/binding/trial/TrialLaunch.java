package com.gueei.android.binding.trial;

import java.lang.reflect.Field;

import org.xmlpull.v1.XmlPullParser;

import viewFactories.ViewFactory;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingActivity;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.listeners.MulticastListener;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;

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
	public class Model{
		Observable<CharSequence> hello = new Observable<CharSequence>("BINDING");
		private int goodbyecount = 0;
		Command goodbyeCommand = new Command(){
			public void Invoke(Object args) {
				hello.set("goodbye: " + goodbyecount++);
			}
		};
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = this.getLayoutInflater();
        // inflater.setFactory(this);
		mBinder = new Binder();
		inflater.setFactory(new ViewFactory(mBinder, new AnotherModel()));
        setContentView(R.layout.main);
    }
    
    
    private Binder mBinder;
	private Object model;

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		Log.d("Binder", "Create View Activity: " + name);
		MulticastListener.Factory.RegisterConstructor(View.OnClickListener.class, OnClickListenerMulticast.class);
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
				
				String onClick = attrs.getAttributeValue("http://code.google.com/p/android-binding", "click");
				Field command = model.getClass().getDeclaredField(onClick);
				Command c = (Command)command.get(model);
				mBinder.bindCommand(view, View.OnClickListener.class, c);
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
}