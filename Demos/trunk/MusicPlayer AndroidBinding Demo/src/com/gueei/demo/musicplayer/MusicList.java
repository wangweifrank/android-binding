package com.gueei.demo.musicplayer;

import java.util.AbstractCollection;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.IObservable;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.collections.ArrayAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

public class MusicList extends Activity {
	private MusicDb mDb;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = new MusicDb(this);
        MusicRowModel2[] rows = new MusicRowModel2[300];
        for(int i=0; i<300; i++){
        	rows[i] = new MusicRowModel2();
        	rows[i].Title.set("Title  " + i);
        }
        try {
			ArrayAdapter<MusicRowModel2> adapter = new ArrayAdapter<MusicRowModel2>(this, rows, R.layout.music_row);
			MusicList2.set(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Binder.setAndBindContentView(this, R.layout.main, this);
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		mDb.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
    	mDb.open();
        populateMusicList();
	}

    
    private void populateMusicList() {
    	MusicList.set(mDb.fetchAllEntries());
	}

    public IObservable<Adapter> MusicList2 = new Observable<Adapter>();
	public IObservable<Boolean> NotScanning = new Observable<Boolean>(true);
    public DependentObservable<String> ScanStatus = new DependentObservable<String>(NotScanning){
		@Override
		public String calculateValue(Object... args) {
			if ((Boolean)args[0])
				return "Scan for files";
			return "Scanning....";
		}
    };
    public IObservable<Cursor> MusicList = new Observable<Cursor>();
    public Command ScanMusic = new Command(){
		public void Invoke(View view, Object... args) {
			doScanMusic();
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
        	Cursor musicCursor = managedQuery
        		(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        	if (musicCursor==null) return 0;
        	musicCursor.moveToFirst();
        	while(musicCursor.moveToNext()){
        		long id = musicCursor.getLong(0);
        		if (db.entryExists(id)) continue;
        		count++;
        		db.saveEntry(id, musicCursor.getString(1), 3, musicCursor.getString(2));
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