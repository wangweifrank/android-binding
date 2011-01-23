package com.gueei.demo.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.cursor.CursorSource;

public class MusicList extends Activity {
	private MusicDb mDb;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Binder", "OnCreate");
        mDb = new MusicDb(this);
        MusicList = new CursorSource(MusicRowModel.class, mDb);
        MusicPlayer.createMusicPlayer(this);
        Binder.setAndBindContentView(this, R.layout.main, this);
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		mDb.close();
		MusicPlayer.getInstance().stop();
	}

	@Override
	protected void onResume() {
		super.onResume();
    	mDb.open();
        populateMusicList();
	}

    
    private void populateMusicList() {
    	MusicList.setCursor(mDb.fetchAllEntries());
	}

	public Observable<Boolean> NotScanning = new Observable<Boolean>(Boolean.class, true);
    public DependentObservable<String> ScanStatus = new DependentObservable<String>(String.class, NotScanning){
		@Override
		public String calculateValue(Object... args) {
			if ((Boolean)args[0])
				return "Scan for files";
			return "Scanning....";
		}
    };
    public CursorSource MusicList;
    public Command ScanMusic = new Command(){
		public void Invoke(View view, Object... args) {
			doScanMusic();
		}
    };
    public Command AboutProject = new Command(){
		public void Invoke(View view, Object... args) {
			Intent intent = new Intent(getApplicationContext(), Explain.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}    	
    };

    private void doScanMusic() {
    	NotScanning.set(false);
    	new ScanMediaTask(this).execute(mDb);
	}
    
    private class ScanMediaTask extends AsyncTask<MusicDb, Integer, Integer> {
    	private Activity mActivity;
    	public ScanMediaTask(Activity Activity){
    		mActivity = Activity;
    	}
    	
    	@Override
    	protected Integer doInBackground(MusicDb... arg0) {
    		MusicDb db = arg0[0];
    		int count = 0;
        	String[] projection = new String[]{
        		MediaStore.Audio.Media._ID,
        		MediaStore.Audio.Media.TITLE,
        		MediaStore.Audio.Media.ARTIST
        	};
        	Cursor musicCursor;
        	try{
        		musicCursor = managedQuery
        		(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        	}catch(Exception e){
        		return 0;
        	}
        	if (musicCursor==null) return 0;
        	musicCursor.moveToFirst();
        	while(musicCursor.moveToNext()){
        		long id = musicCursor.getLong(0);
        		if (db.entryExists(id)) continue;
        		count++;
        		db.createEntry(id, musicCursor.getString(1), 3, musicCursor.getString(2));
        	}
        	musicCursor.close();
        	return count;
    	}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
	    	NotScanning.set(true);
	    	populateMusicList();
	    	Toast.makeText(mActivity, "Added: " + result + " music", Toast.LENGTH_SHORT).show();
		}
    }
}