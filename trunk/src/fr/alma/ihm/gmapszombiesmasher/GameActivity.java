package fr.alma.ihm.gmapszombiesmasher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.listeners.GameOnTouchListener;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAICitizen;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAIZombie;
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
	private long startTime;
	private Map<Integer, Boolean> selectedButton;
	public static final int CHOPPER = 0;
	public static final int BOMB = 1;

	protected static final int SPAWN_CODE = 1;
	protected static final int NEXT_STEP_CODE = 2;
	protected static final int WAIT_CODE = 3;
	protected static final int STOP_CODE = 4;
	protected static final int RESUME_CODE = 5;
	protected static final int REFRESH_MAP_CODE = 6;
	protected static final int CLEAR_MAP_CODE = 7;
	protected static final int END_GAME = 8;
	protected static final long SLEEPING_TIME = 500;
	protected static final long TIME_BEFORE_NEXT_STEP = 100;

	protected static boolean threadStop = false;
	protected static boolean threadSuspended = false;
	protected boolean notSpawn = true;
	protected boolean started = false;
	private GameOnTouchListener onTouchListener;

	public static final String END_GAME_TIME = "time";
	public static final String END_GAME_ZOMBIES_KILLED = "zombiesKilled";
	public static final String END_GAME_CITIZEN_SAVED = "citizenSaved";
	public static final long CHOPPER_LIFE_TIME = 10000;

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		selectedButton = new HashMap<Integer, Boolean>();
		selectedButton.put(CHOPPER, false);
		selectedButton.put(BOMB, false);

		initButtons();

		// Keep Screen On
		ManagePreferences.setKeepScreenOn(this);

		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();

		// Disable controls and set up the view
		mapView.setClickable(false);
		mapView.setFocusable(false);
		onTouchListener = new GameOnTouchListener(this, selectedButton);
		mapView.setOnTouchListener(onTouchListener);
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

	/**
	 * Create the hashmap with the state of each button (selected or not) Add
	 * listeners to the buttons
	 */
	private void initButtons() {

		// Listeners on buttons
		this.findViewById(R.id.helicopter_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (selectedButton.get(CHOPPER)) {
							selectedButton.put(CHOPPER, false);
						} else {
							selectedButton.put(CHOPPER, true);
							putAllOtherToFalse(CHOPPER);
						}
					}
				});
		this.findViewById(R.id.bomb_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (selectedButton.get(BOMB)) {
							selectedButton.put(BOMB, false);
						} else {
							selectedButton.put(BOMB, true);
							putAllOtherToFalse(BOMB);
						}
					}
				});
	}

	/**
	 * Put all the element of the selectedButton hashMap to false excepted
	 * "notThis"
	 * 
	 * @param notThis
	 *            except this element
	 */
	private void putAllOtherToFalse(int notThis) {
		for (int element : this.selectedButton.keySet()) {
			if (element != notThis) {
				this.selectedButton.put(element, false);
			}
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

			Runnable spawLoop = new Runnable() {
				@Override
				public void run() {

					if (notSpawn) {
						while (!mapView.isShown()) {
							SystemClock.sleep(SLEEPING_TIME);
						}
						// Send a message to the handler
						waittingHandler.sendEmptyMessage(SPAWN_CODE);

						while (notSpawn) {
						}

						// Close waitting dialog
						if (dialog.isShowing()) {
							dialog.dismiss();
						}

						startTime = new Date().getTime();
					}
				}
			};

			if (notSpawn) {
				dialog = ProgressDialog.show(GameActivity.this, "",
						"Loading. Please wait...", true);
			}

			Thread spawnTread = new Thread(spawLoop);
			spawnTread.start();

			// Handler waitting for spawn
			waittingHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case SPAWN_CODE:
						spawn();
						// notSpawn = false;
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
					case CLEAR_MAP_CODE:
						mapView.getOverlays().clear();
						break;
					case REFRESH_MAP_CODE:
						mapView.invalidate();
					case END_GAME:
						endGame();
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
		Runnable spawnLoop = new Runnable() {
			@Override
			public void run() {
				int height = mapView.getHeight();
				int width = mapView.getWidth();

				GeoPoint topLeft = mapView.getProjection().fromPixels(0, 0);
				GeoPoint topRight = mapView.getProjection()
						.fromPixels(width, 0);
				GeoPoint botLeft = mapView.getProjection()
						.fromPixels(0, height);
				// GeoPoint botRight =
				// mapView.getProjection().fromPixels(width,height);

				spawn = new Spawn(GameActivity.this, mapView,
						topLeft.getLatitudeE6(), botLeft.getLatitudeE6(),
						topLeft.getLongitudeE6(), topRight.getLongitudeE6());
				spawn.spawnCitizen(ManagePreferences
						.getCitizenNumber(GameActivity.this));
				spawn.spawnZombies(ManagePreferences
						.getZombieNumber(GameActivity.this));
				
				spawn.putOnMap();

				onTouchListener.setSpawn(spawn);
				waittingHandler.sendEmptyMessage(REFRESH_MAP_CODE);
				waittingHandler.sendEmptyMessage(NEXT_STEP_CODE);
			}
		};

		Thread spawnThread = new Thread(spawnLoop);
		spawnThread.start();
	}

	/**
	 * Next step process - Move Citizen and zombie to next step
	 */
	private void nextStep() {

		Runnable nextStepLoop = new Runnable() {
			@Override
			public void run() {
				waittingHandler.sendEmptyMessage(CLEAR_MAP_CODE);

				// Update IA
				for(Entity entity: spawn.getEntities()){
					if(entity.getComponentMap().containsKey(CAICitizen.class.getName())){
						((CAICitizen)entity.getComponentMap().get(CAICitizen.class.getName())).update();
					} else if(entity.getComponentMap().containsKey(CAIZombie.class.getName())) {
						((CAIZombie)entity.getComponentMap().get(CAIZombie.class.getName())).update();
					}
				}
				
				notSpawn = false;
				
				// Put On Map
				spawn.putOnMap();

				waittingHandler.sendEmptyMessage(REFRESH_MAP_CODE);

				// Wait...
				while (threadSuspended) {
					// Do nothing, wait ...
				}
				
				// If not ended
				if(!threadStop){
					waittingHandler.sendEmptyMessage(NEXT_STEP_CODE);
				}

			}
		};

		Thread nextStep = new Thread(nextStepLoop);
		nextStep.start();
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

	/**
	 * Get and return the results of the game to the PlayActivity
	 */
	private void endGame() {
		Intent intent = new Intent();
		intent.putExtra(END_GAME_TIME, new Date().getTime() - startTime);
		intent.putExtra(END_GAME_CITIZEN_SAVED, spawn.getCitizenFree());
		intent.putExtra(END_GAME_ZOMBIES_KILLED, spawn.getZombieKilled());

		setResult(RESULT_OK, intent);
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