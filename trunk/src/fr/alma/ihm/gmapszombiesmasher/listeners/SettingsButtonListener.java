package fr.alma.ihm.gmapszombiesmasher.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import fr.alma.ihm.gmapszombiesmasher.SettingPreferenceActivity;

public class SettingsButtonListener implements OnClickListener {
	
	private Activity parent;

	public SettingsButtonListener(Activity activity){
		this.parent = activity;
	}
	  
	@Override
	public void onClick(View v) {
		Intent intent = new Intent().setClass(this.parent, SettingPreferenceActivity.class);
	    this.parent.startActivity(intent);
	}

}
