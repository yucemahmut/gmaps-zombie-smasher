package fr.alma.ihm.gmapszombiesmasher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.listeners.GameOnTouchListener;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;
import fr.alma.ihm.gmapszombiesmasher.utils.GPSCoordinate;
import fr.alma.ihm.gmapszombiesmasher.utils.GPSUtilities;
import fr.alma.ihm.gmapszombiesmasher.utils.ManageWorlds;
import fr.alma.ihm.gmapszombiesmasher.utils.World;

public class GameActivity extends MapActivity {

	private static final int ZOOM_LEVEL = 18;
	private MapController mapController;
	private MapView mapView;
	private static final int GPS_CODE = 1;
	private static final int SETTINGS_CODE = 2;

	private Handler hRefresh;
	private ProgressDialog dialog;

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();
		// List<Overlay> listOfOverlays = mapView.getOverlays();
		// listOfOverlays.clear();

		// Disable controls and set up the view
		mapView.setClickable(false);
		mapView.setFocusable(false);
		mapView.setOnTouchListener(new GameOnTouchListener(this));
		mapView.setBuiltInZoomControls(false);

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		mapView.setSatellite(settings.getBoolean(
				SettingPreferenceActivity.SATELLITE_VIEW_IN_MAP, false));

		// On récupère l'objet Bundle envoyé par l'autre Activity
		Bundle objetbunble = this.getIntent().getExtras();
		String worldName = objetbunble.getString("selectedWorldName");
		World world = null;
		if (worldName.equals("Play Here")) {

			// Intent to get GPS coordinates
			Intent intent = new Intent().setClass(this, GPSUtilities.class);
			this.startActivityForResult(intent, 1);

		} else {
			world = ManageWorlds.getWorld(worldName);
			mapController.setCenter(new GeoPoint(world.getLatitude(), world
					.getLongitude()));

			mapController.setZoom(world.getZoom());
			mapView.invalidate();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		Runnable showWaitDialog = new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(3000);
				hRefresh.sendEmptyMessage(1);
				dialog.dismiss();
			}
		};

		dialog = ProgressDialog.show(this, "", 
                "Loading. Please wait...", true);
		
		Thread t = new Thread(showWaitDialog);
		t.start();

		// Handler waitting for spawn
		hRefresh = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					spawn();
					break;
				}
			}
		};
	}

	private void spawn() {
		int height = mapView.getHeight();
		int width = mapView.getWidth();
		
		GeoPoint topLeft = mapView.getProjection().fromPixels(0, 0);
		GeoPoint topRight = mapView.getProjection().fromPixels(width, 0);
		GeoPoint botLeft = mapView.getProjection().fromPixels(0, height);
		GeoPoint botRight = mapView.getProjection().fromPixels(width, height);

		Spawn s = new Spawn(this, mapView, topLeft.getLatitudeE6(),
				botLeft.getLatitudeE6(), topLeft.getLongitudeE6(),
				topRight.getLongitudeE6());

		s.spawnCitizen(5);
		s.spawnZombies(5);

		// Reload View
		mapView.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.pause_menu, menu);
		onPause();

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
			Intent intent = new Intent().setClass(this,
					SettingPreferenceActivity.class);
			this.startActivityForResult(intent, 2);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case GPS_CODE:
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Unable to get your current location",
						Toast.LENGTH_LONG).show();
				this.finish();
			} else {
				Bundle objetbunble = data.getExtras();
				GPSCoordinate gpsCoordinate = new GPSCoordinate(
						objetbunble.getDouble("latitude"),
						objetbunble.getDouble("longitude"));

				World world = new World("Here",
						gpsCoordinate.getDecimalLongitude(),
						gpsCoordinate.getDecimalLatitude());

				mapController.setCenter(new GeoPoint(world.getLatitude(), world
						.getLongitude()));
				mapController.setZoom(ZOOM_LEVEL);
			}
			break;
		case SETTINGS_CODE:
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(this);
			mapView.setSatellite(settings.getBoolean(
					SettingPreferenceActivity.SATELLITE_VIEW_IN_MAP, false));
			break;
		default:
			break;
		}

	}

}
