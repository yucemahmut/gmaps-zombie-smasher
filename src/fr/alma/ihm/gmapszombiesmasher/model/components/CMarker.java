package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.R;

/**
 * Define the marker image to be displayed on the map.
 *
 */
public class CMarker implements Component {

	public static final int ZOMBIE_MARKER = R.drawable.zombies_marker;
	public static final int CITIZEN_MARKER = R.drawable.citizens_marker;
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
		case ZOMBIE_MARKER:
			this.idMarker = ZOMBIE_MARKER;
			break;
		case CITIZEN_MARKER:
			this.idMarker = CITIZEN_MARKER;
			break;
		case CHOPPER_MARKER:
			this.idMarker = CHOPPER_MARKER;
			break;
		case BOMB_MARKER:
			this.idMarker = BOMB_MARKER;
			break;
		default:
			break;
		}
	}

}
