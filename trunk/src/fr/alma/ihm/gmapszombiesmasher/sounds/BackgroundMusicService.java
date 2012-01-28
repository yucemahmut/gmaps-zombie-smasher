package fr.alma.ihm.gmapszombiesmasher.sounds;

import fr.alma.ihm.gmapszombiesmasher.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


public class BackgroundMusicService extends Service{

	public static MediaPlayer player;
		
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		player = MediaPlayer.create(this, R.raw.background);
		player.setLooping(true);
		player.setVolume(1.0f, 1.0f);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    // We want this service to continue running until it is explicitly
	    // stopped, so return sticky.
		player.start();

	    return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		player.stop();
	}

}
