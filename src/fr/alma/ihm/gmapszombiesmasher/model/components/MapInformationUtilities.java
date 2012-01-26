package fr.alma.ihm.gmapszombiesmasher.model.components;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

;

public class MapInformationUtilities {

	// ZOOM
	private static final int ZOOM_LEVEL_MIN = 18;
	private static final int ZOOM_LEVEL_MAX = 16;

	private int zoomLevel;
	private double distanceEatMin = 10;
	private double distanceBombMin = 100;
	private double distanceChopperMin = 80;
	private int topLatitude;
	private int botLatitude;
	private int leftLongitude;
	private int rightLongitude;

	public MapInformationUtilities(MapView mapView) {
		this.zoomLevel = mapView.getZoomLevel();

		int height = mapView.getHeight();
		int width = mapView.getWidth();

		GeoPoint topLeft = mapView.getProjection().fromPixels(0, 0);
		GeoPoint topRight = mapView.getProjection().fromPixels(width, 0);
		GeoPoint botLeft = mapView.getProjection().fromPixels(0, height);

		topLatitude = topLeft.getLatitudeE6();
		botLatitude = botLeft.getLatitudeE6();
		leftLongitude = topLeft.getLongitudeE6();
		rightLongitude = topRight.getLongitudeE6();
	}

	/**
	 * @return the zoomLevel
	 */
	public int getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * @return the distanceEatMin
	 */
	public double getDistanceEatMin() {
		return distanceEatMin;
	}

	/**
	 * @return the topLatitude
	 */
	public int getTopLatitude() {
		return topLatitude;
	}

	/**
	 * @return the botLatitude
	 */
	public int getBotLatitude() {
		return botLatitude;
	}

	/**
	 * @return the leftLongitude
	 */
	public int getLeftLongitude() {
		return leftLongitude;
	}

	/**
	 * @return the rightLongitude
	 */
	public int getRightLongitude() {
		return rightLongitude;
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
