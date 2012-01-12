package fr.alma.ihm.gmapszombiesmasher;

import android.app.Activity;
import android.os.Bundle;
import fr.alma.ihm.gmapszombiesmasher.listeners.AchievementsButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.PlayButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.SettingsButtonListener;

public class gMapsZombieSmasher extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		this.findViewById(R.id.play).setOnClickListener(new PlayButtonListener(this));
		//this.findViewById(R.id.level).setOnClickListener(new ManageLevelsButtonListener(this));
		this.findViewById(R.id.achievements).setOnClickListener(new AchievementsButtonListener(this));
		this.findViewById(R.id.settings).setOnClickListener(new SettingsButtonListener(this));
	}
}
