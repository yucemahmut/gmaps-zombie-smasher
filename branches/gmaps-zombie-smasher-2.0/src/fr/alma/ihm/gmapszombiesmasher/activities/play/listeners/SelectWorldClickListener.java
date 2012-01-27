package fr.alma.ihm.gmapszombiesmasher.activities.play.listeners;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.activities.game.GameActivity;
import fr.alma.ihm.gmapszombiesmasher.activities.main.GMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.activities.play.PlayActivity;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;
import fr.alma.ihm.gmapszombiesmasher.utils.ManageWorlds;

public class SelectWorldClickListener implements OnItemClickListener {
	
	private Activity parent;


	public SelectWorldClickListener(Activity activity) {
		this.parent = activity;
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		//On créé un objet Bundle, c'est ce qui va nous permetre d'envoyer des données à l'autre Activity
		Bundle objetbunble = new Bundle();

		//Cela fonctionne plus ou moins comme une HashMap, on entre une clef et sa valeur en face
		if(position == 0){
			objetbunble.putString("selectedWorldName", this.parent.getString(R.string.play_here));
		} else {
			objetbunble.putString("selectedWorldName", ManageWorlds.getWorld(position).getName());
		}
		Intent intent = new Intent().setClass(this.parent, GameActivity.class);
		//On affecte à l'Intent le Bundle que l'on a créé
		intent.putExtras(objetbunble);
		
		GMapsZombieSmasher.soundsManager.playSound(SoundsManager.BOMB_EXPLOSION_2);
		
	    this.parent.startActivityForResult(intent, PlayActivity.PLAY_CODE);
	}

}
