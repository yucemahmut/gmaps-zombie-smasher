package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/**
 * 
 * If the entity has to be displayed on the map, its coordinates are contained
 * in this component
 * 
 */
public class CCoordinates extends Component {

	private int latitude;
	private int longitude;

	public CCoordinates(Entity parent) {
		super(parent);
		latitude = 0;
		longitude = 0;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
}
