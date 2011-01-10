package com.example.binding.demo;

import com.gueei.android.binding.AttributeBinder;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Demo1 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Binder binder = new Binder();
        Binder.init();
        Binder.setAndBindContentView(this, R.layout.main, new Model(this));
    }
    
    public class Model{
    	private Context mContext;
    	public Model(Context context){
    		mContext = context;
    	}
    	public Observable<Boolean> EnableView = new Observable<Boolean>(true);
    	public Observable<CharSequence> ButtonName = new Observable<CharSequence>("Toast");
    	public Command ToastResult = new Command(){
			public void Invoke(View view, Object... args) {
				(Toast.makeText(mContext, EnableView.toString(), Toast.LENGTH_LONG)).show();
			}};
			
		public Command Toggle = new Command(){
			public void Invoke(View view, Object... args) {
				EnableView.set(!EnableView.get());
			}};
    }
}