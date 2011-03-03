package com.gueei.demos.markupDemo.viewModels;

import android.view.View;
import android.widget.Toast;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.observables.ArraySource;

public class SpinnerWithArraySource {
	public final Observable<Object> Selected = new Observable<Object>(Object.class);
	public final ArraySource<String> ContinentNames = new ArraySource<String>();
	public final Command ToastContinent = new Command(){
		public void Invoke(View view, Object... args) {
			Toast.makeText(view.getContext(), Selected.get().toString(), Toast.LENGTH_SHORT).show();
		}
	};
	
	public SpinnerWithArraySource(){
		ContinentNames.setArray(new String[]{
			"Asia", "Africa", "Antarctica", "Australia",
			"Europe", "North America", "South America"
		});
	}
}
