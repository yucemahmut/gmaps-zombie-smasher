package fr.alma.ihm.gmapszombiesmasher.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import fr.alma.ihm.gmapszombiesmasher.GameActivity;

public class SelectWorldClickListener implements OnItemClickListener {
	
	private Activity parent;

	public SelectWorldClickListener(Activity activity) {
		this.parent = activity;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent().setClass(this.parent, GameActivity.class);
	    this.parent.startActivity(intent);
	}
	

}
