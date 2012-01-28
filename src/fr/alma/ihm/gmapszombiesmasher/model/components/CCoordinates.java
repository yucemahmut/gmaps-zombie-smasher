package fr.alma.ihm.gmapszombiesmasher.model.components;

import com.google.android.maps.GeoPoint;

/**
 * 
 * If the entity has to be displayed on the map, its coordinates are contained
 * in this component
 * 
 */
public class CCoordinates implements Component {

	private int latitude;
	private int longitude;
	
	public CCoordinates() {
		this.latitude = 0;
		this.longitude = 0;
	}
	
	public CCoordinates(int latitude, int longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public CCoordinates(GeoPoint geoPoint) {
		this.latitude = geoPoint.getLatitudeE6();
		this.longitude = geoPoint.getLongitudeE6();
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

	/**
	 * Return a new GeoPoint according to the coordinates.
	 * 
	 * @return a new instance of geopoint
	 */
	public GeoPoint getPoint(){
		return new GeoPoint(getLatitude(), getLongitude());
	}

	/**
	 * Return the distance (in meters) between the current coordinates and the
	 * coordinates in parameter.
	 * 
	 * @param coordinates
	 *            the coordinates to compare
	 * @return the distance in meters between the two coordinates
	 */
	public double distanceTo(CCoordinates coordinates) {
		double r = 6378.137;
		double lat1 = (double) latitude / 1000000.0;
		double lon1 = (double) longitude / 1000000.0;
		double lat2 = (double) coordinates.latitude / 1000000.0;
		double lon2 = (double) coordinates.longitude / 1000000.0;

		double tetaLat = lat2 - lat1;
		double tetaLon = lon2 - lon1;

		double deltaLat = tetaLat * r * (Math.PI / 180);
		double deltaLon = tetaLon * r * Math.cos(tetaLat) * (Math.PI / 180);

		double d = (Math.sqrt(Math.pow(deltaLat, 2) + Math.pow(deltaLon, 2))) * 1000;

		return d;
	}

	/**
	 * Return true or false if this is near of the <i>goalCoordinates</i>
	 * according to the <i>distanceMin</i> in meters.
	 * 
	 * @param goalCoordinates
	 *            the coordinates to compare.
	 * @param distanceMin
	 *            the minimum distance to qualify the coordinates has "near".
	 * @return true if the distance is lower that <i>distanceMin</i>.
	 */
	public boolean isNearOf(CCoordinates goalCoordinates, double distanceMin) {
		return distanceTo(goalCoordinates) <= distanceMin;
	}
	
	public String toString(){
		return "Lat: " + getLatitude() + " - Long: " + getLongitude();
	}
}
