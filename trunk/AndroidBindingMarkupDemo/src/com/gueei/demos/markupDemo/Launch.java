package com.gueei.demos.markupDemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import com.gueei.demos.markupDemo.custom_actionbar.ActCustomAB;
import gueei.binding.Binder;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;

public class Launch extends Activity {

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Binder.init(this.getApplication());
		DemoGroups.add(
				new DemoGroup("Simple Views", 
						new Demo("View"), 
						new Demo("TextView"), 
						new Demo("ImageView"),
						new Demo("ProgressBar"), 
						new Demo("SeekBar"), 
						new Demo("RatingBar"),
						new Demo("CompoundButton"), 
						new Demo("CustomView", true)));
		DemoGroups.add(new DemoGroup("Menu", 
				new Demo("OptionsMenu", true, OptionsMenu.class),
				new Demo("ContextMenu", true),
				new Demo("Custom Action Bar", true, ActCustomAB.class)));

		DemoGroups.add(new DemoGroup("Application related", 
				new Demo("ResourceLinking", true),
				new Demo("Parceling", true, Parceling.class)
		));
		DemoGroups.add(new DemoGroup("Simple Lists", 
				new Demo("ArrayListAsListViewSource"),
				new Demo("SpinnerWithArraySource"),
				new Demo("DependantSpinnersWithCursorSource", true),
				new Demo("ListViewWithCursorSource", true),
				new Demo("SingleChoiceList"), 
				new Demo("MultipleChoiceList"),
				new Demo("FilterArrayList", true),
				new Demo("FilterCursorList", true)
				));
		DemoGroups
				.add(new DemoGroup("Compound/Nested Lists", new Demo("MasterDetailListView"),
								   new Demo("NestedCursor"),
								   new Demo("CursorContentUpdate", true), new Demo("MultipleAdapters")));
		DemoGroups.add(new DemoGroup("Dynamic Lists", 
				new Demo("LazyLoadArrayList", true),
				new Demo("DynamicLoadingArrayList", true)
				));
		DemoGroups.add(new DemoGroup("Animation", new Demo("AnimationTrigger", true)));
		Binder.setAndBindContentView(this, R.layout.select_demo, this);
	}

	public final Command ViewDemo  = new Command() {
		public void Invoke(View view, Object... args) {
			if (SelectedDemo.get() == null) {
				return;
			}
			Demo selection = (Demo) SelectedDemo.get();
			Intent intent;
			if (selection.DemoActivity != null) {
				intent = new Intent(Launch.this, selection.DemoActivity);
			}
			else {
				intent = new Intent(Launch.this, ViewDemoActivity.class);
			}
			intent.putExtra("DEMO", selection.Name.get());
			Launch.this.startActivity(intent);
		}
	};
	public final Command ShowAbout = new Command() {
		public void Invoke(View view, Object... args) {
			WebView wv = new WebView(Launch.this);
			wv.loadData(Launch.this.getResources().getString(R.string.explain), "text/html", "UTF-8");
			AlertDialog.Builder builder = new AlertDialog.Builder(Launch.this).setView(wv).setTitle("About...");
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	};

	public static class DemoGroup {

		public       StringObservable          Title = new StringObservable();
		public final ArrayListObservable<Demo> Demos = new ArrayListObservable<Demo>(Demo.class);

		public DemoGroup(String title, Demo... demos) {
			Title.set(title);
			for (int i = 0; i < demos.length; i++) {
				Demos.add(demos[i]);
			}
		}
	}

	public final Observable<Object>             SelectedDemo = new Observable<Object>(Object.class);
	public final ArrayListObservable<DemoGroup> DemoGroups   = new ArrayListObservable<DemoGroup>(DemoGroup.class);

	public static class Demo {

		public StringObservable  Name        = new StringObservable();
		public BooleanObservable NewAddition = new BooleanObservable();
		public Class<?> DemoActivity;

		public Demo(String name, boolean newAddition, Class<?> activity) {
			this(name, newAddition);
			DemoActivity = activity;
		}

		public Demo(String name, boolean newAddition) {
			Name.set(name);
			NewAddition.set(newAddition);
		}

		public Demo(String name) {
			this(name, false);
		}
	}
}