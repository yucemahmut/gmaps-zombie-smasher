package fr.alma.ihm.gmapszombiesmasher.model.components;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

;

public class MapInformationUtilities {

	// ZOOM
	public static final int ZOOM_LEVEL_MIN = 18;
	public static final int ZOOM_LEVEL_MAX = 16;

	private int zoomLevel;
	private double distanceEatMin = 10;
	private double distanceBombMin = 100;
	private double distanceChopperMin = 80;
	private MapView mapView;

	public MapInformationUtilities(MapView mapView) {
		this.zoomLevel = mapView.getZoomLevel();
		this.mapView = mapView;
	}

	/**
	 * @return the zoomLevel
	 */
	public int getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * @return the topLatitude
	 */
	public int getTopLatitude() {
		GeoPoint topLeft = mapView.getProjection().fromPixels(0, 0);
		return topLeft.getLatitudeE6();
	}

	/**
	 * @return the botLatitude
	 */
	public int getBotLatitude() {
		GeoPoint botLeft = mapView.getProjection().fromPixels(0, mapView.getHeight());
		return botLeft.getLatitudeE6();
	}

	/**
	 * @return the leftLongitude
	 */
	public int getLeftLongitude() {
		GeoPoint topLeft = mapView.getProjection().fromPixels(0, 0);
		return topLeft.getLongitudeE6();
	}

	/**
	 * @return the rightLongitude
	 */
	public int getRightLongitude() {
		GeoPoint topRight = mapView.getProjection().fromPixels(mapView.getWidth(), 0);
		return topRight.getLongitudeE6();
	}

	public double getDistanceToEatMin() {
		return distanceEatMin * (getZoomLevelMin() - zoomLevel + 1);
	}

	public double getDistanceBombMin() {
		return distanceBombMin * (getZoomLevelMin() - zoomLevel + 1);
	}

	public double getDistanceChopperMin() {
		return distanceChopperMin * (getZoomLevelMin() - zoomLevel + 1);
	}

	public int getZoomLevelMin() {
		return ZOOM_LEVEL_MIN;
	}
	
	public int getZoomLevelMax() {
		return ZOOM_LEVEL_MAX;
	}
}
