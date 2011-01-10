package com.example.binding.demo;

import java.util.AbstractCollection;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;

public class Demo1CodeModel {
	private Context mContext;
	
	public Observable<Adapter> ImageList;
	public Observable<NameDrawablePair> Selection;
	public DependentObservable<Drawable> ImageToShow;
	
	public Demo1CodeModel(Context context){
		mContext = context.getApplicationContext();
		NameDrawablePair[] pairs = new NameDrawablePair[]{ 
				new NameDrawablePair(
						"Show the layout markup (demo1.xml)",
						context.getResources().getDrawable(R.drawable.demo_layout)),
				new NameDrawablePair(
						"Show the model (DemoModel1.java)",
						context.getResources().getDrawable(R.drawable.demo_model)),
				new NameDrawablePair(
						"Show the activity (Demo1.java)",
						context.getResources().getDrawable(R.drawable.demo_activity))
		};
		Selection = new Observable<NameDrawablePair>(pairs[0]);
		Adapter list = 
			new ArrayAdapter<NameDrawablePair>
				(context, android.R.layout.simple_spinner_dropdown_item, pairs);
		ImageList = new Observable<Adapter>(list);
		
		ImageToShow = 
			new DependentObservable<Drawable>(Selection){
				@Override
				public Drawable calculateValue(Object... args) {
					return ((NameDrawablePair)args[0]).drawable;
				}
		};
	}
	
	public class NameDrawablePair{
		public String name;
		public Drawable drawable;
		
		@Override
		public String toString() {
			return name;
		}

		public NameDrawablePair(String name, Drawable drawable) {
			this.name = name;
			this.drawable = drawable;
		}
	}
}
