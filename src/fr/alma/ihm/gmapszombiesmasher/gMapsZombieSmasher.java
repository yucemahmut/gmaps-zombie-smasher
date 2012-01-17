package fr.alma.ihm.gmapszombiesmasher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import fr.alma.ihm.gmapszombiesmasher.listeners.AchievementsButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.PlayButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.SettingsButtonListener;
import fr.alma.ihm.gmapszombiesmasher.sounds.BackgroundMusicService;
import fr.alma.ihm.gmapszombiesmasher.utils.ManagePreferences;

public class gMapsZombieSmasher extends Activity {
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
		
		// start to play background music
		// *****important***** don't forget to call stopService when application goes to background
		this.startService(new Intent(this, BackgroundMusicService.class));
	}

	private void checkInternetConnection() {
		if(!ManagePreferences.isOnline(this)){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("You need an Internet connection to play this game." +
					"Please, enable your internet connection.")
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


}
