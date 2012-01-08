package fr.alma.ihm.gmapszombiesmasher;

import java.util.List;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

import fr.alma.ihm.gmapszombiesmasher.listeners.GameOnTouchListener;
//import fr.alma.ihm.gmapszombiesmasher.views.ZombiesOverlay;

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
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		
		
		// Disable controls and set up the view
		mapView.setClickable(false);
		mapView.setFocusable(false);
		mapView.setOnTouchListener(new GameOnTouchListener());
		
		mapView.setBuiltInZoomControls(false);
		mapController.setCenter(new GeoPoint(40769800, -73960500));
		mapController.setZoom(ZOOM_LEVEL);
		
		// Overlays
//		
//		ZombiesOverlay zO = new ZombiesOverlay();
//		
//		listOfOverlays.add(zO);
//		
	       mapView.invalidate();
		//mapView.setOnTouchListener(new mapTouchListener());
	}

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();

    inflater.inflate(R.menu.pause_menu, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.it_resume:
        // TODO
        return true;

      case R.id.it_achievements:
        // TODO
        return true;

      case R.id.it_settings:
        // TODO
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
