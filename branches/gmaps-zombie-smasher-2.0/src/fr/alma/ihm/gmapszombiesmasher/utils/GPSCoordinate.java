package fr.alma.ihm.gmapszombiesmasher.utils;

public class GPSCoordinate {
	
	private double latitude;
	private double longitude;
	
	public GPSCoordinate(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	
	public int getDecimalLatitude(){
		return (int) (this.latitude * 1E6);
	}
	
	public int getDecimalLongitude(){
		return (int) (this.longitude * 1E6);
	}
}
