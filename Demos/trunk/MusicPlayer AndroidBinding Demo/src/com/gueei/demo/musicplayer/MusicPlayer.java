package com.gueei.demo.musicplayer;

import com.gueei.android.binding.Observable;

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

public class MusicPlayer extends Service {
	private static MusicPlayer instance;
	
	private MediaPlayer player;
	
	public Observable<Boolean> isPlaying = new Observable<Boolean>(Boolean.class, false);
		
	public static void createMusicPlayer(Context context){
		synchronized(MusicPlayer.class){
			if (instance==null){
				Intent intent = new Intent();
				intent.setClass(context, MusicPlayer.class);
				context.startService(intent);
			}
		}
	}
	
	public static MusicPlayer getInstance(){
		synchronized(MusicPlayer.class){
			return instance;
		}
	}
	
	public void play(long id, String title){
		player.stop();
		try {
			isPlaying.set(true);
			player.release();
			player = new MediaPlayer();
			player.setDataSource(getApplicationContext(), 
					Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id + ""));
			player.prepare();
			player.start();
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	
	public void stop(){
		player.stop();
		isPlaying.set(false);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		synchronized(MusicPlayer.class){
			super.onCreate();
			instance = this;
			player = new MediaPlayer();
		}
	}
}
