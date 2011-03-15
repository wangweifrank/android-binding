package com.gueei.demos.markupDemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.observables.ArraySource;

public class Launch extends Activity {
	
	private final String[] AVAILABLE_DEMOS = {
		"View", "TextView", "ImageView", "ProgressBar", "RatingBar", 
		"CompoundButton", "SpinnerWithArraySource", "ListViewWithCursorSource",
		"Converters", "MasterDetailListView"
	};
	
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
        
        Demos.setArray(AVAILABLE_DEMOS);
        Binder.setAndBindContentView(this, R.layout.select_demo, this);
    }
    
    public final Command ViewDemo = new Command(){
		public void Invoke(View view, Object... args) {
			if (SelectedDemo.get() == null) return;
			String selection = SelectedDemo.get().toString();
			Intent intent = new Intent(Launch.this, ViewDemoActivity.class);
			intent.putExtra("DEMO", selection);
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
    
    public final ArraySource<String> Demos = new ArraySource<String>();
}