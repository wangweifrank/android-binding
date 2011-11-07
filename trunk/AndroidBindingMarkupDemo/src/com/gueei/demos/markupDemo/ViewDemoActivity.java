package com.gueei.demos.markupDemo;

import gueei.binding.Binder;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.observables.IntegerObservable;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ViewDemoActivity extends Activity {
	String demoName;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        demoName = this.getIntent().getExtras().getString("DEMO");
        if ((demoName==null) || (demoName.length()==0)){
        	finish();
        }
        
        Binder.InflateResult result = Binder.inflateView(this, R.layout.view_demo, null, false);
		Binder.bindView(this, result, this);
		setContentView(result.rootView);
        try{
        	loadDemo(demoName);
        }catch(Exception e){
        	e.printStackTrace();
        	finish();
        }
    }
    
    private CodeView markupView, codeView;
    private boolean markupLoaded = false;
    private boolean codeLoaded = false;
    
    private void loadDemo(String name) throws Exception{
    	int demoLayout = R.layout.class.getField(name.toLowerCase()).getInt(null);
    	String modelClass = "com.gueei.demos.markupDemo.viewModels." + name;
    	Object model;
    	try{
    		model = Class.forName(modelClass).newInstance();
    	}catch(Exception e){
    		model = Class.forName(modelClass).getConstructor(Activity.class).newInstance(this);
    	}
    	
    	View view = Binder.bindView(this, 
        		Binder.inflateView(this, demoLayout, null, false), model);
    	
    	markupView = CodeView.create(this);
    	codeView = CodeView.create(this);
        DisplayingView.set(new View[]{view, markupView, codeView});
        DisplayingViewIndex.set(0);
        this.setTitle("Demo: " + demoName);
    }
    
    public final Observable<View[]> DisplayingView = new Observable<View[]>(View[].class);
    public final IntegerObservable DisplayingViewIndex = new IntegerObservable(0);
    public final IntegerObservable MarkupIndex = new IntegerObservable(1);
    public final IntegerObservable CodeIndex = new IntegerObservable(2);
    public final IntegerObservable DemoIndex = new IntegerObservable(0);
    
    public final Command ViewMarkup = new Command(){
		public void Invoke(View view, Object... args) {
			if (!markupLoaded){
				new Thread(){
					@Override
					public void run(){
						try{
							int id = R.raw.class.getField(demoName.toLowerCase()).getInt(null);
							markupView.setCodeResource(demoName.toLowerCase()+ ".xml", id, "xml");
						}catch(Exception e){}
						finally{markupLoaded = true;}
					}
				}.start();
			}
	    	DisplayingViewIndex.set(MarkupIndex.get());
		}
    };
    public final Command ViewDemo = new Command(){
		public void Invoke(View view, Object... args) {
	    	DisplayingViewIndex.set(DemoIndex.get());
		}
    };
    public final Command ViewCode = new Command(){
		public void Invoke(View view, Object... args) {
			if (!codeLoaded){
				new Thread(){
					@Override
					public void run(){
						try{
							int id = R.raw.class.getField(demoName.toLowerCase()+"_code").getInt(null);
							codeView.setCodeResource(demoName+ ".java", id, "java");
						}catch(Exception e){}
						finally{codeLoaded = true;}
					}
				}.start();
			}
	    	DisplayingViewIndex.set(CodeIndex.get());
		}
    };
}