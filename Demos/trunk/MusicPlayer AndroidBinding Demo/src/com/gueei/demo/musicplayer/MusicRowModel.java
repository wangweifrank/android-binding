package com.gueei.demo.musicplayer;

import com.gueei.android.binding.cursor.CursorRowModel;
import com.gueei.android.binding.cursor.FloatField;
import com.gueei.android.binding.cursor.IdField;
import com.gueei.android.binding.cursor.IntegerField;
import com.gueei.android.binding.cursor.StringField;

public class MusicRowModel extends CursorRowModel {
	public StringField Title = new StringField(1);
	public StringField Artist = new StringField(3);
	public IdField Id = new IdField(0);
	public FloatField Rating = new FloatField(2);
}
