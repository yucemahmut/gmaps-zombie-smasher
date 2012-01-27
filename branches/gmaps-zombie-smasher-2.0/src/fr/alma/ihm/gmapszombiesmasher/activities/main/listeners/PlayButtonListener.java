package fr.alma.ihm.gmapszombiesmasher.activities.main.listeners;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import fr.alma.ihm.gmapszombiesmasher.activities.main.GMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.activities.play.PlayActivity;
import fr.alma.ihm.gmapszombiesmasher.sounds.BackgroundMusicService;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;

public class PlayButtonListener implements OnClickListener {

	private Activity parent;

	public PlayButtonListener(Activity activity) {
		this.parent = activity;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent().setClass(this.parent, PlayActivity.class);
        this.parent.startActivity(intent);
        parent.stopService(new Intent(parent, BackgroundMusicService.class));
        GMapsZombieSmasher.soundsManager.playSound(SoundsManager.BOMB_EXPLOSION_2);

	}

}
