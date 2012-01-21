package fr.alma.ihm.gmapszombiesmasher.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import fr.alma.ihm.gmapszombiesmasher.SelectWorldInMap;
import fr.alma.ihm.gmapszombiesmasher.gMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;

public class ManageLevelsButtonListener implements OnClickListener {

	private Activity parent;


	public ManageLevelsButtonListener(Activity activity) {
		this.parent = activity;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent().setClass(this.parent, SelectWorldInMap.class);
	
		gMapsZombieSmasher.soundsManager.playSound(SoundsManager.EXPLOSION_2);
	    
		this.parent.startActivity(intent);
		
	}
}
