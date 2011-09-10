package com.gueei.demos.markupDemo.viewModels;

import android.view.View;
import android.widget.Toast;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.collections.HashMapObservable;
import gueei.binding.observables.IntegerObservable;

public class SpinnerWithArraySource {

	public final Observable<Object>                 AbsSpinnerSelectedItemDef     =
			new Observable<Object>(Object.class);
	public final ArrayListObservable<String>        ContinentNames                =
			new ArrayListObservable<String>(String.class);
	public final HashMapObservable<Integer, String> ContinentNames2               =
			new HashMapObservable<Integer, String>(Integer.class, String.class);
	public final IntegerObservable                  AbsSpinnerSelectedPositionDef = new IntegerObservable(2);
	public final Command                            ToastContinent                = new Command() {
		public void Invoke(View view, Object... args) {
	//		Toast.makeText(view.getContext(), AbsSpinnerSelectedItemDef.get().toString(), Toast.LENGTH_SHORT).show();
		}
	};

	public SpinnerWithArraySource() {
		ContinentNames.setArray(
				new String[]{"Asia", "Africa", "Antarctica", "Australia", "Europe", "North America", "South America"});
	}
}
