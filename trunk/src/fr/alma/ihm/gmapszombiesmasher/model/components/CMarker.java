package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;

public class CMarker extends Component {

	public static final int ZOMBIE_MARKER = R.drawable.zombies_marker;
	public static final int CITIZEN_MARKER = R.drawable.citizens_marker;
	public static final int CHOPPER_MARKER = R.drawable.chopper_0_marker;
	public static final int BOMB_MARKER = R.drawable.explosion_0_marker;
	
	private int marker;
	
	public CMarker(Entity parent) {
		super(parent);
	}
	
	public int getMarker(){
		return marker;
	}
	
	public void setZombie(){
		this.marker = ZOMBIE_MARKER;
	}
	
	public void setCitizen(){
		this.marker = CITIZEN_MARKER;
	}
	
	public void setChopper(){
		this.marker = CHOPPER_MARKER;
	}

	public void setBomb() {
		this.marker = BOMB_MARKER;
	}

}
