package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.model.managers.FactoryManager;

/**
 * Define the marker image to be displayed on the map.
 *
 */
public class CMarker implements Component {

	public static final int ZOMBIE_MARKER = R.drawable.zombie_marker_2;
	public static final int CITIZEN_MARKER = R.drawable.citizen_marker_2;
	public static final int CHOPPER_MARKER = R.drawable.chopper_0_marker;
	public static final int BOMB_MARKER = R.drawable.explosion_0_marker;
	
	private int idMarker;
	
	public CMarker(int idMarker){
		this.idMarker = idMarker;
	}
	
	public int getIdMarker(){
		return idMarker;
	}
	
	public void setIdMarker(int id){
		switch (id) {
		case FactoryManager.ZOMBIES:
			this.idMarker = ZOMBIE_MARKER;
			break;
		case FactoryManager.CITIZEN:
			this.idMarker = CITIZEN_MARKER;
			break;
		default:
			this.idMarker = id;
			break;
		}
	}

}
