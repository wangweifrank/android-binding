package com.gueei.demos.markupDemo.viewModels;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.cursor.CursorRowModel;
import com.gueei.android.binding.cursor.CursorSource;
import com.gueei.android.binding.cursor.StringField;

public class ListViewWithCursorSource {
	public ListViewWithCursorSource(Activity activity){
		Cursor c = activity.getContentResolver().query(
				MediaStore.Audio.Media.INTERNAL_CONTENT_URI, 
				new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE}, 
				null, null, null);
		activity.startManagingCursor(c);
		MusicList.setCursor(c);
	}
	
	private final CursorRowModel.Factory<MusicItem> itemFactory = new CursorRowModel.Factory<MusicItem>(){
		public MusicItem createRowModel(Context context) {
			return new MusicItem();
		}
	};
	
	public final CursorSource<MusicItem> MusicList = 
		new CursorSource<MusicItem>(MusicItem.class, itemFactory);
	
	public final Observable<Object> ClickedItem = new Observable<Object>(Object.class);
	public final Command ItemClickedCommand = new Command(){
		public void Invoke(View view, Object... args) {
			Toast.makeText(view.getContext(), ((MusicItem)ClickedItem.get()).Title.get(), Toast.LENGTH_SHORT)
				.show();
		}
	};
	
	public class MusicItem extends CursorRowModel{
		public final StringField Title = new StringField(1);
		@Override
		public void resetInternalState(int position) {
		}
	}
}
