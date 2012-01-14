package fr.alma.ihm.gmapszombiesmasher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import fr.alma.ihm.gmapszombiesmasher.utils.ManagePreferences;
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
	protected static final long SLEEPING_TIME = 3000;
	protected static final long TIME_BEFORE_NEXT_STEP = 3000;
	private static final int CITIZEN_NUMBER = 5;
	private static final int ZOMBIES_NOMBER = 5;

	protected static boolean threadStop = false;
	protected static boolean threadSuspended = false;
	protected boolean notSpawn = true;
	protected boolean started = false;

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		// Keep Screen On
		ManagePreferences.setKeepScreenOn(this);

		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();

		// Disable controls and set up the view
		mapView.setClickable(false);
		mapView.setFocusable(false);
		mapView.setOnTouchListener(new GameOnTouchListener(this));
		mapView.setBuiltInZoomControls(false);

		mapView.setSatellite(ManagePreferences.isSateliteView(this));

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
		super.onStop();
		System.out.println("OnStop");
		waittingHandler.sendEmptyMessage(STOP_CODE);
	}

	protected void startMainLoop() {
		if (!started) {
			mapView.getOverlays().clear();
			started = true;

			Runnable mainLoop = new Runnable() {
				@Override
				public void run() {

					if (notSpawn) {
						notSpawn = false;
						SystemClock.sleep(SLEEPING_TIME);
						// Close waitting dialog
						dialog.dismiss();
						// Send a message to the handler
						waittingHandler.sendEmptyMessage(SPAWN_CODE);
					}

					SystemClock.sleep(SLEEPING_TIME);

					while (!threadStop) {
						while (threadSuspended) {
							// Do nothing, wait ...
						}
						SystemClock.sleep(TIME_BEFORE_NEXT_STEP);
						// Send a message to the handler
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

	/**
	 * Create and place citizen and zombies on the map Set a goal for each
	 */
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

		spawn.spawnCitizen(CITIZEN_NUMBER);
		spawn.spawnZombies(ZOMBIES_NOMBER);

		for (Entity citizen : spawn.getCitizens()) {
			setNewGoal(citizen);
			spawn.putCitizenOnMap(citizen);
		}

		for (Entity zombie : spawn.getZombies()) {
			setNewGoal(zombie);
			spawn.putZombieOnMap(zombie);
		}

		// Reload View
		mapView.invalidate();
	}

	/**
	 * Next step process - Move Citizen and zombie to next step
	 */
	private void nextStep() {
		CGoal goal = null;
		CCoordinates coordinates = null;
		// Clear map
		mapView.getOverlays().clear();

		// For each citizen, go to next step
		for (Entity citizen : spawn.getCitizens()) {
			goal = (CGoal) citizen.getComponentMap().get(CGoal.class.getName());
			coordinates = goal.getNextPosition(0);
			if (!goal.goalReached()) {
				citizen.addComponent(coordinates);
				spawn.putCitizenOnMap(citizen);
			} else {
				// If the goal is reached, get a new one:
				setNewGoal(citizen);
				spawn.putCitizenOnMap(citizen);
			}
		}

		// For each zombie, go to next step
		for (Entity zombie : spawn.getZombies()) {
			goal = (CGoal) zombie.getComponentMap().get(CGoal.class.getName());
			coordinates = goal.getNextPosition(0);
			if (!goal.goalReached()) {
				zombie.addComponent(coordinates);
				spawn.putZombieOnMap(zombie);
			} else {
				// If the goal is reached, get a new one:
				setNewGoal(zombie);
				spawn.putZombieOnMap(zombie);
			}
		}

		mapView.invalidate();
	}

	/**
	 * Set a random new goal to the entity and set the current position on the
	 * road closer to the goal
	 * 
	 * @param entity
	 *            the entity to set
	 */
	private void setNewGoal(Entity entity) {
		// Get New Goal Coordinates
		Entity newGoal = new Entity();
		newGoal.addComponent(spawn.getRandomPosition(newGoal));

		// Put the new goal on the Entity
		CGoal goal = (CGoal) entity.getComponentMap()
				.get(CGoal.class.getName());
		entity.addComponent(goal.setGoal(newGoal));
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
			mapView.setSatellite(ManagePreferences.isSateliteView(this));
			break;
		default:
			break;
		}
	}
}
