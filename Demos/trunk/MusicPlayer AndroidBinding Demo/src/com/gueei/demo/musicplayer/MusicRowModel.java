package com.gueei.demo.musicplayer;

import android.view.View;
import android.widget.Toast;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.cursor.CursorRowModel;
import com.gueei.android.binding.cursor.FloatField;
import com.gueei.android.binding.cursor.IdField;
import com.gueei.android.binding.cursor.StringField;

public class MusicRowModel extends CursorRowModel {
	public StringField Title = new StringField(1);
	public StringField Artist = new StringField(3);
	public IdField Id = new IdField(0);
	public FloatField Rating = new FloatField(2);
	
	public Command Save = new Command(){
		public void Invoke(View view, Object... args) {
			Toast.makeText(getContext(), "Saving " + Title.get(), Toast.LENGTH_SHORT).show();
			((MusicDb)getParameters()[0]).updateEntry(Id.get(), Title.get(), Rating.get(), Artist.get());
			getCursor().requery();
		}
	};
	
	public Command Play = new Command(){
		public void Invoke(View view, Object... args){
			MusicPlayer.getInstance().play(Id.get(), Title.get());
		}
	};

	@Override
	public void resetInternalState(int position) {
	}
}
