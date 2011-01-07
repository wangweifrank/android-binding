package com.gueei.android.binding.trial;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;

public class OtherModel {
	public Observable<Boolean> Economy = new Observable<Boolean>(false);
	public Observable<Boolean> Business = new Observable<Boolean>(false);
	public Observable<Boolean> FirstClass = new Observable<Boolean>(false);
	public Observable<Boolean> DetailEnabled = new Observable<Boolean>(false);
	public Observable<Object> Selection = new Observable<Object>("selection");
	public Command DumpSelection = new Command(){
		public void Invoke(View view, Object... args) {
			Log.e("Binder", Selection.get().toString());
		}
	};
	
	public Observable<Adapter> ClassSelection;
	
	public OtherModel(Context context){
		ArrayAdapter adapter = new ArrayAdapter(context, 
					android.R.layout.simple_spinner_dropdown_item, 
					new Integer[]{123, 456, 789});
		ClassSelection = new Observable<Adapter>(adapter);
	}
}
