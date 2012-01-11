package fr.alma.ihm.gmapszombiesmasher.listeners;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import fr.alma.ihm.gmapszombiesmasher.GameActivity;
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
			objetbunble.putString("selectedWorldName", "Play Here");
		} else {
			objetbunble.putString("selectedWorldName", ManageWorlds.getWorld(position).getName());
		}
		Intent intent = new Intent().setClass(this.parent, GameActivity.class);
		//On affecte à l'Intent le Bundle que l'on a créé
		intent.putExtras(objetbunble);
		
	    this.parent.startActivity(intent);
	}
	

}
