package fr.alma.ihm.gmapszombiesmasher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.KeyEvent;
import android.view.WindowManager;
import fr.alma.ihm.gmapszombiesmasher.listeners.AchievementsButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.PlayButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.SettingsButtonListener;
import fr.alma.ihm.gmapszombiesmasher.sounds.BackgroundMusicService;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManagerFactory;
import fr.alma.ihm.gmapszombiesmasher.utils.ManagePreferences;

public class gMapsZombieSmasher extends Activity {
		
	public static SoundsManager soundsManager;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		checkInternetConnection();
		
		this.findViewById(R.id.play).setOnClickListener(new PlayButtonListener(this));
		//this.findViewById(R.id.level).setOnClickListener(new ManageLevelsButtonListener(this));
		this.findViewById(R.id.achievements).setOnClickListener(new AchievementsButtonListener(this));
		this.findViewById(R.id.settings).setOnClickListener(new SettingsButtonListener(this));
		
		// get sound manager
		soundsManager = SoundsManagerFactory.get(this);
		
		// set preference
		updatePreference();
	}

	private void checkInternetConnection() {
		if(!ManagePreferences.isOnline(this)){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.internet_connection))
			       .setCancelable(false)
			       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   gMapsZombieSmasher.this.finish();
			           }
			       });
			
			AlertDialog alert = builder.create();
			alert.show();
			
		}
	}
	
	@Override
    public void onAttachedToWindow() {
		// disable home button
    	this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
    	super.onAttachedToWindow();
    
    }

	// detection of the HOME_KEY event and BACK_KEY event
    @Override
	public boolean onKeyDown (int keyCode, KeyEvent event){

    	if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
			this.stopService(new Intent(this, BackgroundMusicService.class));
		//	android.os.Process.killProcess(android.os.Process.myPid());
			android.os.Process.sendSignal(android.os.Process.myPid(), android.os.Process.SIGNAL_KILL);
		}
		return true;
	}
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	updatePreference();
    }
	
    private void updatePreference() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	// update background music setting
    	if(prefs.getBoolean(SettingPreferenceActivity.BACKGROUND_MUSIC, true)) {
    		this.startService(new Intent(this, BackgroundMusicService.class));
    	} else {
    		this.stopService(new Intent(this, BackgroundMusicService.class));
    	}
    	
    	// update game sound setting
    	gMapsZombieSmasher.soundsManager.setSoundOn(prefs.getBoolean(SettingPreferenceActivity.APPLICATION_SOUNDS, true));
    	
    	// update satellite setting
    	if(prefs.getBoolean(SettingPreferenceActivity.SATELLITE_VIEW_IN_MAP, false)) {
    		// to do ...
    		
    	} else {
    		// to do ...
    	}
    }

    public void exit(View v) {
      android.os.Process.sendSignal(android.os.Process.myPid(), android.os.Process.SIGNAL_KILL);
    }
}
