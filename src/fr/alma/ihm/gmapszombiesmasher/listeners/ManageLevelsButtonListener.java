package fr.alma.ihm.gmapszombiesmasher.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import fr.alma.ihm.gmapszombiesmasher.SelectWorldInMap;

public class ManageLevelsButtonListener implements OnClickListener {

	private Activity parent;

	public ManageLevelsButtonListener(Activity activity) {
		this.parent = activity;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent().setClass(this.parent, SelectWorldInMap.class);
	    this.parent.startActivity(intent);
		
	}
}
