package com.example.binding.demo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;

public class DemoModel1 {
	private Context mContext;
	public DemoModel1(Context context){
		mContext = context.getApplicationContext();
		Adapter list = 
			new ArrayAdapter<String>
				(context, android.R.layout.simple_spinner_dropdown_item,
						new String[]{ "Mr.", "Ms.", "Dr.", "Pr." });
		TitleList = new Observable<Adapter>(list);
	}
	
	public Observable<String> FirstName = new Observable<String>("");
	public Observable<String> LastName = new Observable<String>("");
	public Observable<String> Title = new Observable<String>("");
	public Observable<String> Email = new Observable<String>("");
	public Observable<Adapter> TitleList;
	public DependentObservable<String> FullName = new DependentObservable<String>(FirstName, LastName, Title){
		@Override
		public String calculateValue(Object... arg) {
			return arg[2] + " " + arg[0] + " " + arg[1];
		}
	};
	public Command ShowResult = new Command(){
		public void Invoke(View arg0, Object... arg1) {
			Toast toast = Toast.makeText(mContext, "You entered: " + FullName.get(), Toast.LENGTH_LONG);
			toast.show();
		}};
	
	public Command ShowNameHelp = new Command(){
		public void Invoke(View arg0, Object... arg1) {
			Toast toast = Toast.makeText(mContext, 
					"As you are typing, the model is automatically updated.", 
					Toast.LENGTH_SHORT);
			toast.show();
		}};
		
	public Command ShowEmailHelp = new Command(){
			public void Invoke(View arg0, Object... arg1) {
				Toast toast = Toast.makeText(mContext, 
						"It works with android internal view engine. As the email is formatting using " +
						"Android:autoLink", 
						Toast.LENGTH_SHORT);
				toast.show();
			}};	

	public Command RunCodeActivity = new Command(){
		public void Invoke(View arg0, Object... arg1) {
			Intent intent = new Intent();
			intent.setClass(mContext, Demo1Code.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}};

	public Command RunExplainActivity = new Command(){
		public void Invoke(View arg0, Object... arg1) {
			Intent intent = new Intent();
			intent.setClass(mContext, Explain.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}};
			

}
