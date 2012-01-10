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

  public boolean equals(CCoordinates c) {
    return (latitude == c.latitude && longitude == c.longitude);
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

  // http://fr.wikipedia.org/wiki/Calcul_distance_ente_deux_coordonnées_géographiques
  public double distanceTo(CCoordinates c) {
    double r = 6378.137;
    double lat1 = (double) latitude / 1000000.0;
    double lon1 = (double) longitude / 1000000.0;
    double lat2 = (double) c.latitude / 1000000.0;
    double lon2 = (double) c.longitude / 1000000.0;

    double tetaLat = lat2 - lat1;
    double tetaLon = lon2 - lon1;

    double deltaLat = tetaLat * r * (Math.PI / 180);
    double deltaLon = tetaLon * r * Math.cos(tetaLat) * (Math.PI / 180);

    double d = (Math.sqrt(Math.pow(deltaLat, 2) + Math.pow(deltaLon, 2))) * 1000;

    return d;
  }
}
