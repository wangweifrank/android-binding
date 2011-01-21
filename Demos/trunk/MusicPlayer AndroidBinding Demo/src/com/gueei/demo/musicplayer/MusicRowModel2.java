package com.gueei.demo.musicplayer;

import android.view.View;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.cursor.CursorRowModel;
import com.gueei.android.binding.cursor.FloatField;
import com.gueei.android.binding.cursor.IdField;
import com.gueei.android.binding.cursor.IntegerField;
import com.gueei.android.binding.cursor.StringField;

public class MusicRowModel2 extends CursorRowModel {
	public Observable<String> Title = new Observable<String>("HELLO");
	public Observable<String> Artist = new Observable<String>("Artist");
	public Observable<Float> Rating = new Observable<Float>(3f);
	public Observable<Boolean> DisplayRating = new Observable<Boolean>(false);
	public Command ToggleRating = new Command(){
		public void Invoke(View view, Object... args) {
			DisplayRating.set(!DisplayRating.get());
		}
	};
}
