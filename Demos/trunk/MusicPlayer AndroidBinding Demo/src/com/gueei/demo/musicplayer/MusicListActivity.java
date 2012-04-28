package com.gueei.demo.musicplayer;

import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import gueei.binding.collections.CursorCollection;
import gueei.binding.cursor.FloatField;
import gueei.binding.cursor.IRowModelFactory;
import gueei.binding.cursor.IdField;
import gueei.binding.cursor.RowModel;
import gueei.binding.cursor.StringField;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class MusicListActivity extends BindingActivity {
	private MusicDb mDb;
	private MusicListViewModel mViewModel;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Binder", "OnCreate");
        mDb = new MusicDb(this);
        mViewModel = new MusicListViewModel(this);
        this.setAndBindRootView(R.layout.main, mViewModel);
        bindService(new Intent(this, MusicPlayerService.class), 
        		mConnection, Context.BIND_AUTO_CREATE);
    	mDb.open();
    	mViewModel.onDbOpened();
    }
    
	@Override
	protected void onDestroy() {
		mDb.close();
		unbindService(mConnection);
		super.onDestroy();
	}
    
    private MusicPlayerService musicPlayer;
    
    private ServiceConnection mConnection = new ServiceConnection(){
		public void onServiceConnected(ComponentName name, IBinder service) {
			musicPlayer = ((MusicPlayerService.MusicPlayerBinder)service).getMusicPlayer();
		}

		public void onServiceDisconnected(ComponentName name) {
			musicPlayer = null;
		}
    };
    
    
    
    public class MusicListViewModel{
    	private final Activity mActivity;
    	public MusicListViewModel(Activity activity){
            mActivity = activity;
            if (musicPlayer!=null)
            	IsPlaying.set(musicPlayer.isPlaying());
    	}
    	
    	public void onDbOpened(){
    		populateMusicList();
    	}

    	
    	public void play(long id, String title){
    		if (musicPlayer!=null){
    			musicPlayer.play(id, title);
    			IsPlaying.set(true);
    		}
    	}
    	
    	private void populateMusicList() {
        	MusicList.setCursor(mDb.fetchAllEntries());
    	}

    	public BooleanObservable IsPlaying = new BooleanObservable(false);
    	public BooleanObservable NotScanning = new BooleanObservable(true);

    	public IntegerObservable Scanned = new IntegerObservable(0);
    	
    	public CursorCollection<MusicRowModel> MusicList = 
        	new CursorCollection<MusicRowModel>(MusicRowModel.class, 
        			new IRowModelFactory<MusicRowModel>(){
						@Override
						public MusicRowModel createInstance() {
							return new MusicRowModel(MusicListActivity.this);
						}
        	});
        
        public class MusicRowModel extends RowModel{
        	private final Context mContext;
        	public MusicRowModel(Context context){
        		mContext = context;
        	}
        	
        	public final IdField Id = new IdField("_id");
        	public final StringField Title = new StringField("title");
        	public final StringField Artist = new StringField("artist");
        	public final FloatField Rating = new FloatField("rating");
        	
        	public Command Save = new Command(){
        		public void Invoke(View view, Object... args) {
        			Toast.makeText(mContext, "Saving " + Title.get(), Toast.LENGTH_SHORT).show();
        			mDb.updateEntry(Id.get(), Title.get(), Rating.get(), Artist.get());
        			MusicList.getCursor().requery();
        		}
        	};
        	
        	public Command Play = new Command(){
        		public void Invoke(View view, Object... args){
        			play(Id.get(), Title.get());
        		}
        	};
        }
        
        //public CursorSource<MusicRowModel> MusicList;
        public Command ScanMusic = new Command(){
    		public void Invoke(View view, Object... args) {
    			doScanMusic();
    		}
        };
        public Command Stop = new Command(){
    		public void Invoke(View view, Object... args) {
    			if (musicPlayer!=null){
        			musicPlayer.stop();
        			IsPlaying.set(false);
        		}
    		}        	
        };
        public Command AboutProject = new Command(){
    		public void Invoke(View view, Object... args) {
    			Intent intent = new Intent(getApplicationContext(), Explain.class);
    			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    			startActivity(intent);
    		}
        };
        
        private void doScanMusic(){
        	NotScanning.set(false);
        	ScanMediaTask task = new ScanMediaTask(mActivity);
        	task.execute(mDb);
        }
        
        private class ScanMediaTask extends AsyncTask<MusicDb, Integer, Integer> {
        	private final Activity mActivity;
        	public ScanMediaTask(Activity activity){
        		mActivity = activity;
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
            		this.publishProgress(count);
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

			@Override
			protected void onPreExecute() {
				Scanned.set(0);
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				Scanned.set(values[0]);
			}
        }
    }    
}