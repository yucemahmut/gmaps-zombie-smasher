package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;

public class CMarker extends Component {

	private static final int zombieMarker = R.drawable.zombies_marker;
	private static final int citizenMarker = R.drawable.citizens_marker;
	private static final int chopperMarker = R.drawable.chopper_marker;
	
	private int marker;
	
	public CMarker(Entity parent) {
		super(parent);
	}
	
	public int getMarker(){
		return marker;
	}
	
	public void setZombie(){
		this.marker = zombieMarker;
	}
	
	public void setCitizen(){
		this.marker = citizenMarker;
	}
	
	public void setChopper(){
		this.marker = chopperMarker;
	}

}
