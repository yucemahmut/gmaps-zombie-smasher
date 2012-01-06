package fr.alma.ihm.gmapszombiesmasher;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.listeners.mapClickListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.mapTouchListener;

public class GameActivity extends MapActivity {

	private static final int ZOOM_LEVEL = 18;

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		MapController mapController = mapView.getController();
		
		mapView.setBuiltInZoomControls(false);
		mapView.setClickable(false);
		mapView.setOnTouchListener(new mapTouchListener());
		mapController.setZoom(ZOOM_LEVEL);
		mapController.setCenter(new GeoPoint(40769800, -73960500));

	}
}
