package com.gueei.demos.markupDemo;

import gueei.binding.Binder;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class Launch extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binder.init(this.getApplication());
        
        /*
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
			XhtmlRendererFactory.getRenderer("xml").highlight("main", 
					getResources().openRawResource(R.raw.main), 
					bos, "UTF-8", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Display.set(bos.toString());
        
		WebView wv = (WebView)this.findViewById(R.id.wv);
		wv.loadData(bos.toString(), "text/html", "UTF-8");
		*/
        
        Demos.add(new Demo("View", true));
        Demos.add(new Demo("TextView"));
        Demos.add(new Demo("ImageView"));
        Demos.add(new Demo("ProgressBar"));
        Demos.add(new Demo("SeekBar", true));
        Demos.add(new Demo("RatingBar"));
        Demos.add(new Demo("CompoundButton"));
        Demos.add(new Demo("SpinnerWithArraySource"));
        Demos.add(new Demo("ListViewWithCursorSource"));
        Demos.add(new Demo("MasterDetailListView"));
        Demos.add(new Demo("NestedCursor"));
        Demos.add(new Demo("MultipleAdapters"));
        
        Binder.setAndBindContentView(this, R.layout.select_demo, this);
    }
    
    public final Command ViewDemo = new Command(){
		public void Invoke(View view, Object... args) {
			if (SelectedDemo.get() == null) return;
			Demo selection = (Demo)SelectedDemo.get();
			Intent intent = new Intent(Launch.this, ViewDemoActivity.class);
			intent.putExtra("DEMO", selection.Name.get());
			Launch.this.startActivity(intent);
		}
    };
    
    public final Command ShowAbout = new Command(){
    	public void Invoke(View view, Object... args) {
			WebView wv = new WebView(Launch.this);
			wv.loadData(Launch.this.getResources().getString(R.string.explain), "text/html", "UTF-8");
			AlertDialog.Builder builder = new AlertDialog.Builder(Launch.this)
				.setView(wv)
				.setTitle("About...");
			AlertDialog dialog = builder.create();
			dialog.show();
		}
    };
    
    public final Observable<Object> SelectedDemo = new Observable<Object>(Object.class);

    public final ArrayListObservable<Demo> Demos = new
    	ArrayListObservable<Demo>(Demo.class);
    
    public static class Demo{
    	public StringObservable Name = new StringObservable();
    	public BooleanObservable NewAddition = new BooleanObservable();
    	public Demo(String name, boolean newAddition){
    		Name.set(name);
    		NewAddition.set(newAddition);
    	}
    	public Demo(String name){
    		this(name, false);
    	}
    }
}