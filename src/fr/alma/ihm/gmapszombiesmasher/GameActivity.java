package fr.alma.ihm.gmapszombiesmasher;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
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
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CGoal;
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

	private Spawn spawn;
	private Handler waittingHandler;
	private ProgressDialog dialog;
	private Thread mainTread;

	private static final int SPAWN_CODE = 1;
	private static final int NEXT_STEP_CODE = 2;
	protected static final int WAIT_CODE = 3;
	protected static final int STOP_CODE = 4;
	protected static final int RESUME_CODE = 5;

	protected static boolean threadStop = false;
	protected static boolean threadSuspended = false;
	protected boolean notSpawn = true;
	protected boolean started = false;
	
	 private PowerManager.WakeLock wakeLock;

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		
		// Unallow lock screen
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                this.getClass().getName());
        
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        lock.disableKeyguard();

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
		startMainLoop();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("OnPause");
		waittingHandler.sendEmptyMessage(WAIT_CODE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("OnResume");
		waittingHandler.sendEmptyMessage(RESUME_CODE);
	}

	@Override
	protected void onStop() {
		System.out.println("OnStop");
		super.onStop();
		waittingHandler.sendEmptyMessage(STOP_CODE);
	}

	protected void startMainLoop() {
		super.onStart();

		if (!started) {
			started = true;

			System.out.println("Spawn: " + notSpawn);
			System.out.println("threadStop: " + threadStop);
			System.out.println("threadSuspended: " + threadSuspended);

			Runnable mainLoop = new Runnable() {
				@Override
				public void run() {

					if (notSpawn) {
						SystemClock.sleep(3000);
						waittingHandler.sendEmptyMessage(SPAWN_CODE);
						dialog.dismiss();
						notSpawn = false;
					}

					while (!threadStop) {
						while (threadSuspended) {
							//
						}
						SystemClock.sleep(500);
						waittingHandler.sendEmptyMessage(NEXT_STEP_CODE);
					}
				}
			};

			if (notSpawn) {
				dialog = ProgressDialog.show(GameActivity.this, "",
						"Loading. Please wait...", true);
			}

			mainTread = new Thread(mainLoop);
			mainTread.start();

			// Handler waitting for spawn
			waittingHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case SPAWN_CODE:
						spawn();
						break;
					case NEXT_STEP_CODE:
						nextStep();
						break;
					case WAIT_CODE:
						threadSuspended = true;
						break;
					case STOP_CODE:
						threadStop = true;
						started = false;
						break;
					case RESUME_CODE:
						threadStop = false;
						threadSuspended = false;
						break;
					}
				}
			};
		}
	}

	private void spawn() {
		int height = mapView.getHeight();
		int width = mapView.getWidth();

		GeoPoint topLeft = mapView.getProjection().fromPixels(0, 0);
		GeoPoint topRight = mapView.getProjection().fromPixels(width, 0);
		GeoPoint botLeft = mapView.getProjection().fromPixels(0, height);
		GeoPoint botRight = mapView.getProjection().fromPixels(width, height);

		spawn = new Spawn(this, mapView, topLeft.getLatitudeE6(),
				botLeft.getLatitudeE6(), topLeft.getLongitudeE6(),
				topRight.getLongitudeE6());

		spawn.spawnCitizen(5);
		spawn.spawnZombies(5);

		Entity center = new Entity();
		CCoordinates coordinates = new CCoordinates(center);
		coordinates.setLatitude(mapView.getMapCenter().getLatitudeE6());
		coordinates.setLongitude(mapView.getMapCenter().getLongitudeE6());
		center.addComponent(coordinates);

		CGoal goal;
		for (Entity citizen : spawn.getCitizens()) {
			goal = (CGoal) citizen.getComponentMap().get(CGoal.class.getName());
			spawn.putCitizenOnMap(goal.setGoal(center));
		}

		// Reload View
		mapView.invalidate();
	}

	private void nextStep() {

		System.out.println("NEXT STEP");
		CGoal goal = null;

		for (Entity citizen : spawn.getCitizens()) {
			goal = (CGoal) citizen.getComponentMap().get(CGoal.class.getName());
			spawn.putCitizenOnMap(goal.getNextPosition(0));
		}

		mapView.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		waittingHandler.sendEmptyMessage(WAIT_CODE);
		inflater.inflate(R.menu.pause_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		waittingHandler.sendEmptyMessage(WAIT_CODE);
		return true;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		waittingHandler.sendEmptyMessage(RESUME_CODE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.it_resume:
			waittingHandler.sendEmptyMessage(RESUME_CODE);
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
