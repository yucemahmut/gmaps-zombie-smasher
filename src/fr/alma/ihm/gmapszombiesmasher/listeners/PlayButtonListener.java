package fr.alma.ihm.gmapszombiesmasher.listeners;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import fr.alma.ihm.gmapszombiesmasher.PlayActivity;
import fr.alma.ihm.gmapszombiesmasher.sounds.BackgroundMusicService;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManagerFactory;

public class PlayButtonListener implements OnClickListener {

	private Activity parent;
	private SoundsManager soundManager;

	public PlayButtonListener(Activity activity) {
		this.parent = activity;
		soundManager = SoundsManagerFactory.get(activity);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent().setClass(this.parent, PlayActivity.class);
        this.parent.startActivity(intent);
        parent.stopService(new Intent(parent, BackgroundMusicService.class));
        soundManager.playSound(SoundsManager.EXPLOSION);

	}

}
