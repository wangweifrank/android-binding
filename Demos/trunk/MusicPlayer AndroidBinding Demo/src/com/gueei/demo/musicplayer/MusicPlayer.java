package com.gueei.demo.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

public class MusicPlayer {
	private Context mContext;
	private static MusicPlayer instance;
	
	private MediaPlayer player;
	
	private MusicPlayer(Context context){
		mContext = context;
		player = new MediaPlayer();
	}
	
	public static void createMusicPlayer(Context context){
		if (instance==null)
			instance = new MusicPlayer(context);
	}
	
	public static MusicPlayer getInstance(){
		return instance;
	}
	
	public void play(long id){
		player.stop();
		try {
			player.release();
			player = new MediaPlayer();
			player.setDataSource(mContext, 
					Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id + ""));
			player.prepare();
			player.start();
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	
	public void stop(){
		player.stop();
	}
}
