package fr.alma.ihm.gmapszombiesmasher.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import fr.alma.ihm.gmapszombiesmasher.SettingPreferenceActivity;
import fr.alma.ihm.gmapszombiesmasher.gMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;

public class SettingsButtonListener implements OnClickListener {
	
	private Activity parent;


	public SettingsButtonListener(Activity activity){
		this.parent = activity;
	}
	  
	@Override
	public void onClick(View v) {
		Intent intent = new Intent().setClass(this.parent, SettingPreferenceActivity.class);
		
		gMapsZombieSmasher.soundsManager.playSound(SoundsManager.SHIOO);
	    
		this.parent.startActivity(intent);
	}

}
