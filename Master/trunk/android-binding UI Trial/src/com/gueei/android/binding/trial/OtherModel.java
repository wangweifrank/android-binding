package com.gueei.android.binding.trial;

import android.content.Context;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.gueei.android.binding.Observable;

public class OtherModel {
	public Observable<Boolean> Economy = new Observable<Boolean>(false);
	public Observable<Boolean> Business = new Observable<Boolean>(false);
	public Observable<Boolean> FirstClass = new Observable<Boolean>(false);
	public Observable<Boolean> DetailEnabled = new Observable<Boolean>(false);
	
	public Observable<Adapter> ClassSelection;
	
	public OtherModel(Context context){
		ArrayAdapter adapter = new ArrayAdapter(context, 
					android.R.layout.simple_spinner_dropdown_item, 
					new String[]{"April", "Betty", "Celin"});
		ClassSelection = new Observable<Adapter>(adapter);
	}
}
