package fr.alma.ihm.gmapszombiesmasher;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.utils.ManageWorlds;
import fr.alma.ihm.gmapszombiesmasher.utils.World;

public class EditWorldInMap extends MapActivity {

	private MapController mapController;
	private MapView mapView;
	private World world;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_world);

		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();

		mapView.setBuiltInZoomControls(true);

		// On récupère l'objet Bundle envoyé par l'autre Activity
		Bundle objetbunble = this.getIntent().getExtras();
		String worldName = objetbunble.getString("worldEditName");
		world = ManageWorlds.getWorld(worldName);
		System.out.println("World name: " + worldName);
		System.out.println("World: " + world);
		mapController.setCenter(new GeoPoint(world.getLatitude(), world
				.getLongitude()));
		mapController.setZoom(world.getZoom());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.edit_world_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit_ok:
			world.setLatitude(mapView.getMapCenter().getLatitudeE6());
			world.setLongitude(mapView.getMapCenter().getLongitudeE6());
			world.setZoom(mapView.getZoomLevel());
			setResult(RESULT_OK);
			finish();
			return true;
		case R.id.edit_cancel:
			setResult(RESULT_CANCELED);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
