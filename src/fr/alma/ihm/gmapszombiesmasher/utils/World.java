package fr.alma.ihm.gmapszombiesmasher.utils;

import java.io.Serializable;

import fr.alma.ihm.gmapszombiesmasher.GameActivity;

public class World implements Serializable {
	
	private static final long serialVersionUID = 2605987111959296080L;
	
	private int longitude;
	private int latitude;
	private int zoom;
	private String name;
	
	public World (String name, int longitude, int latitude){
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public int getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public int getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the zoom
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * @param zoom the zoom to set
	 */
	public void setZoom(int zoom) {
		if(zoom < GameActivity.ZOOM_LEVEL_MAX){
			zoom = GameActivity.ZOOM_LEVEL_MAX;
		} else if(zoom > GameActivity.ZOOM_LEVEL_MIN){
			zoom = GameActivity.ZOOM_LEVEL_MIN;
		}
		this.zoom = zoom;
	}
	
	
}
