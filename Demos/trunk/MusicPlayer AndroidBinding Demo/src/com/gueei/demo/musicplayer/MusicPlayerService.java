package com.gueei.demo.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;

public class MusicPlayerService extends Service {
	public class MusicPlayerBinder extends Binder{
		MusicPlayerService getMusicPlayer(){
			return MusicPlayerService.this;
		}
	}
	
	private MediaPlayer player;
	
	public boolean mPlaying = false;
		
	public void play(long id, String title){
		player.stop();
		try {
			mPlaying = true;
			player.release();
			player = new MediaPlayer();
			player.setDataSource(getApplicationContext(), 
					Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id + ""));
			player.prepare();
			player.start();
			
			Notification notification = 
				new Notification(R.drawable.icon, "Music Player", System.currentTimeMillis());
			notification.flags = Notification.FLAG_ONGOING_EVENT;
			PendingIntent intent = 
				PendingIntent.getActivity(this, 0, new Intent(this, MusicListActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(this, "Playing: " + title, "Music Player currently playing", intent);
			((NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, notification);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void stop(){
		player.stop();
		mPlaying = false;
		((NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
	}

	public boolean isPlaying(){
		return mPlaying;
	}
	
	private final IBinder mBinder = new MusicPlayerBinder();
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		synchronized(MusicPlayerService.class){
			super.onCreate();
			player = new MediaPlayer();
		}
	}
}
